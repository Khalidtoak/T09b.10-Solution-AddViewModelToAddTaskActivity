/*
* Copyright (C) 2016 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.android.todolist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.View

import com.example.android.todolist.database.AppDatabase
import com.example.android.todolist.database.TaskEntry

import android.support.v7.widget.DividerItemDecoration.VERTICAL


class MainActivity : AppCompatActivity(), TaskAdapter.ItemClickListener {
    // Member variables for the adapter and RecyclerView
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: TaskAdapter? = null

    private var mDb: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set the RecyclerView to its corresponding view
        mRecyclerView = findViewById(R.id.recyclerViewTasks)

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView!!.layoutManager = LinearLayoutManager(this)

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = TaskAdapter(this, this)
        mRecyclerView!!.adapter = mAdapter

        val decoration = DividerItemDecoration(applicationContext, VERTICAL)
        mRecyclerView!!.addItemDecoration(decoration)

        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            // Called when a user swipes left or right on a ViewHolder
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                // Here is where you'll implement swipe to delete
                AppExecutors.instance!!.diskIO().execute {
                    val position = viewHolder.adapterPosition
                    val tasks = mAdapter!!.tasks
                    mDb!!.taskDao().deleteTask(tasks!![position])
                }
            }
        }).attachToRecyclerView(mRecyclerView)

        /*
         Set the Floating Action Button (FAB) to its corresponding View.
         Attach an OnClickListener to it, so that when it's clicked, a new intent will be created
         to launch the AddTaskActivity.
         */
        val fabButton = findViewById<FloatingActionButton>(R.id.fab)

        fabButton.setOnClickListener {
            // Create a new intent to start an AddTaskActivity
            val addTaskIntent = Intent(this@MainActivity, AddTaskActivity::class.java)
            startActivity(addTaskIntent)
        }

        mDb = AppDatabase.getInstance(applicationContext)
        setupViewModel()
    }

    private fun setupViewModel() {
        val viewModel = ViewModelProviders.of(this).get<MainViewModel>(MainViewModel::class.java!!)
        viewModel.tasks.observe(this, Observer { taskEntries ->
            Log.d(TAG, "Updating list of tasks from LiveData in ViewModel")
            mAdapter!!.tasks = taskEntries
        })
    }

    override fun onItemClickListener(itemId: Int) {
        // Launch AddTaskActivity adding the itemId as an extra in the intent
        val intent = Intent(this@MainActivity, AddTaskActivity::class.java)
        intent.putExtra(EXTRA_TASK_ID, itemId)
        startActivity(intent)
    }

    companion object {

        // Constant for logging
        private val TAG = MainActivity::class.java.simpleName
    }
}
