package com.example.android.todolist

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.util.Log

import com.example.android.todolist.database.AppDatabase
import com.example.android.todolist.database.TaskEntry

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val tasks: LiveData<List<TaskEntry>>

    init {
        val database = AppDatabase.getInstance(this.getApplication())
        Log.d(TAG, "Actively retrieving the tasks from the DataBase")
        tasks = database!!.taskDao().loadAllTasks()
    }

    companion object {

        // Constant for logging
        private val TAG = MainViewModel::class.java.simpleName
    }
}
