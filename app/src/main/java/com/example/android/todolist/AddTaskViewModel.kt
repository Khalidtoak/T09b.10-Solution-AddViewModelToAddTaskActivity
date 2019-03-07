package com.example.android.todolist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

import com.example.android.todolist.database.AppDatabase
import com.example.android.todolist.database.TaskEntry

// COMPLETED (5) Make this class extend ViewModel
class AddTaskViewModel// COMPLETED (8) Create a constructor where you call loadTaskById of the taskDao to initialize the tasks variable
// Note: The constructor should receive the database and the taskId
(database: AppDatabase, taskId: Int) : ViewModel() {

    // COMPLETED (6) Add a task member variable for the TaskEntry object wrapped in a LiveData
    // COMPLETED (7) Create a getter for the task variable
    val task: LiveData<TaskEntry> = database.taskDao().loadTaskById(taskId)

}
