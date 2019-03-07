package com.example.android.todolist.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

@Dao
interface TaskDao {

    @Query("SELECT * FROM task ORDER BY priority")
    fun loadAllTasks(): LiveData<List<TaskEntry>>

    @Insert
    fun insertTask(taskEntry: TaskEntry)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTask(taskEntry: TaskEntry)

    @Delete
    fun deleteTask(taskEntry: TaskEntry)

    @Query("SELECT * FROM task WHERE id = :id")
    fun loadTaskById(id: Int): LiveData<TaskEntry>
}
