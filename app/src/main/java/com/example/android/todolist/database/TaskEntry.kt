package com.example.android.todolist.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

import java.util.Date

@Entity(tableName = "task")
data class TaskEntry(
        var description: String?,
        var priority: Int,
        @ColumnInfo(name = "updated_at")
       var updatedAt: Date?,
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0
)