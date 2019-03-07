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
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup

import com.example.android.todolist.database.AppDatabase
import com.example.android.todolist.database.TaskEntry

import java.util.Date
// Extra for the task ID to be received in the intent
const val EXTRA_TASK_ID = "extraTaskId"
// Extra for the task ID to be received after rotation
const val INSTANCE_TASK_ID = "instanceTaskId"
// Constants for priority
const val PRIORITY_HIGH = 1
const val PRIORITY_MEDIUM = 2
const val PRIORITY_LOW = 3
// Constant for default task id to be used when not in update mode
const val DEFAULT_TASK_ID = -1

class AddTaskActivity : AppCompatActivity() {
    // Fields for views
    lateinit var mEditText: EditText
    private lateinit var mRadioGroup: RadioGroup
    lateinit var mButton: Button

    private var mTaskId = DEFAULT_TASK_ID

    // Member variable for the Database
    private var mDb: AppDatabase? = null

    /**
     * getPriority is called whenever the selected priority needs to be retrieved
     */
    private val priorityFromViews: Int
        get() {
            var priority = 1
            val checkedId = (findViewById<View>(R.id.radioGroup) as RadioGroup).checkedRadioButtonId
            when (checkedId) {
                R.id.radButton1 -> priority = PRIORITY_HIGH
                R.id.radButton2 -> priority = PRIORITY_MEDIUM
                R.id.radButton3 -> priority = PRIORITY_LOW
            }
            return priority
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        initViews()

        mDb = AppDatabase.getInstance(applicationContext)

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID)
        }

        val intent = intent
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            mButton.setText(R.string.update_button)
            if (mTaskId == DEFAULT_TASK_ID) {
                // populate the UI
                mTaskId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID)

                // COMPLETED (9) Remove the logging and the call to loadTaskById, this is done in the ViewModel now
                // COMPLETED (10) Declare a AddTaskViewModelFactory using mDb and mTaskId
                val factory = AddTaskViewModelFactory(mDb!!, mTaskId)
                // COMPLETED (11) Declare a AddTaskViewModel variable and initialize it by calling ViewModelProviders.of
                // for that use the factory created above AddTaskViewModel
                val viewModel = ViewModelProviders.of(this, factory).get<AddTaskViewModel>(AddTaskViewModel::class.java!!)

                // COMPLETED (12) Observe the LiveData object in the ViewModel. Use it also when removing the observer
                viewModel.task.observe(this, object : Observer<TaskEntry> {
                    override fun onChanged(taskEntry: TaskEntry?) {
                        viewModel.task.removeObserver(this)
                        populateUI(taskEntry)
                    }
                })
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(INSTANCE_TASK_ID, mTaskId)
        super.onSaveInstanceState(outState)
    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    private fun initViews() {
        mEditText = findViewById(R.id.editTextTaskDescription)
        mRadioGroup = findViewById(R.id.radioGroup)

        mButton = findViewById(R.id.saveButton)
        mButton.setOnClickListener { onSaveButtonClicked() }
    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param task the taskEntry to populate the UI
     */
    private fun populateUI(task: TaskEntry?) {
        if (task == null) {
            return
        }

        mEditText.setText(task.description)
        setPriorityInViews(task.priority)
    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    fun onSaveButtonClicked() {
        val description = mEditText.text.toString()
        val priority = priorityFromViews
        val date = Date()

        val task = TaskEntry(description, priority, date)
        AppExecutors.instance!!.diskIO().execute {
            if (mTaskId == DEFAULT_TASK_ID) {
                // insert new task
                mDb!!.taskDao().insertTask(task)
            } else {
                //update task
                task.id = mTaskId
                mDb!!.taskDao().updateTask(task)
            }
            finish()
        }
    }

    /**
     * setPriority is called when we receive a task from MainActivity
     *
     * @param priority the priority value
     */
    private fun setPriorityInViews(priority: Int) {
        when (priority) {
            PRIORITY_HIGH -> (findViewById<View>(R.id.radioGroup) as RadioGroup).check(R.id.radButton1)
            PRIORITY_MEDIUM -> (findViewById<View>(R.id.radioGroup) as RadioGroup).check(R.id.radButton2)
            PRIORITY_LOW -> (findViewById<View>(R.id.radioGroup) as RadioGroup).check(R.id.radButton3)
        }
    }



}
