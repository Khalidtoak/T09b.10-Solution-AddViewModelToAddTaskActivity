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

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.android.todolist.database.TaskEntry

import java.text.SimpleDateFormat
import java.util.Locale

/**
 * This TaskAdapter creates and binds ViewHolders, that hold the description and priority of a task,
 * to a RecyclerView to efficiently display data.
 */
// Constant for date format
private const val DATE_FORMAT = "dd/MM/yyy"
class TaskAdapter

/**
 * Constructor for the TaskAdapter that initializes the Context.
 *
 * @param context  the current Context
 * @param listener the ItemClickListener
 */
(private val mContext: Context, // Member variable to handle item clicks
 private val mItemClickListener: ItemClickListener) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    // Class variables for the List that holds task data and the Context
    /**
     * When data changes, this method updates the list of taskEntries
     * and notifies the adapter to use the new values on it
     */
    var tasks: List<TaskEntry>? = null
        set(taskEntries) {
            field = taskEntries
            notifyDataSetChanged()
        }
    // Date formatter
    private val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new TaskViewHolder that holds the view for each task
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // Inflate the task_layout to a view
        val view = LayoutInflater.from(mContext)
                .inflate(R.layout.task_layout, parent, false)

        return TaskViewHolder(view)
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        // Determine the values of the wanted data
        val (description, priority, updatedAt1) = tasks!![position]
        val updatedAt = dateFormat.format(updatedAt1)

        //Set values
        holder.taskDescriptionView.text = description
        holder.updatedAtView.text = updatedAt

        // Programmatically set the text and color for the priority TextView
        val priorityString = "" + priority // converts int to String
        holder.priorityView.text = priorityString

        val priorityCircle = holder.priorityView.background as GradientDrawable
        // Get the appropriate background color based on the priority
        val priorityColor = getPriorityColor(priority)
        priorityCircle.setColor(priorityColor)
    }

    /*
    Helper method for selecting the correct priority circle color.
    P1 = red, P2 = orange, P3 = yellow
    */
    private fun getPriorityColor(priority: Int): Int {
        var priorityColor = 0

        when (priority) {
            1 -> priorityColor = ContextCompat.getColor(mContext, R.color.materialRed)
            2 -> priorityColor = ContextCompat.getColor(mContext, R.color.materialOrange)
            3 -> priorityColor = ContextCompat.getColor(mContext, R.color.materialYellow)
            else -> {
            }
        }
        return priorityColor
    }

    /**
     * Returns the number of items to display.
     */
    override fun getItemCount(): Int {
        return if (tasks == null) {
            0
        } else tasks!!.size
    }

    interface ItemClickListener {
        fun onItemClickListener(itemId: Int)
    }

    // Inner class for creating ViewHolders
     inner class TaskViewHolder
    /**
     * Constructor for the TaskViewHolders.
     *
     * @param itemView The view inflated in onCreateViewHolder
     */
    (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        // Class variables for the task description and priority TextViews
        var taskDescriptionView: TextView = itemView.findViewById(R.id.taskDescription)
        var updatedAtView: TextView = itemView.findViewById(R.id.taskUpdatedAt)
        var priorityView: TextView = itemView.findViewById(R.id.priorityTextView)

        init {

            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val elementId = tasks!![adapterPosition].id
            mItemClickListener.onItemClickListener(elementId)
        }
    }



}