package com.example.android.todolist

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

import com.example.android.todolist.database.AppDatabase

// COMPLETED (1) Make this class extend ViewModel ViewModelProvider.NewInstanceFactory
@Suppress("UNCHECKED_CAST")
class AddTaskViewModelFactory// COMPLETED (3) Initialize the member variables in the constructor with the parameters received
(// COMPLETED (2) Add two member variables. One for the database and one for the taskId
        private val mDb: AppDatabase, private val mTaskId: Int) : ViewModelProvider.NewInstanceFactory() {

    // COMPLETED (4) Uncomment the following method
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return AddTaskViewModel(mDb, mTaskId) as T
    }
}
