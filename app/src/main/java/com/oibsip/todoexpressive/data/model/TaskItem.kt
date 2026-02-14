package com.oibsip.todoexpressive.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ownerId: Long,
    val title: String,
    val details: String,
    val category: String,
    val isCompleted: Boolean = false
)
