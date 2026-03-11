package com.example.cia3.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Task Entity representing a single task in the Room database.
 * This is the Model layer of the MVVM architecture.
 */
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val taskId: Int = 0,
    val title: String,
    val description: String,
    val dueDate: String,
    val isCompleted: Boolean = false,
    val completedDate: String? = null
)
