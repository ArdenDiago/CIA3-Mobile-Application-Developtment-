package com.example.cia3

import android.app.Application
import com.example.cia3.data.database.TaskDatabase
import com.example.cia3.data.repository.TaskRepository

/**
 * Application class for initializing the database and repository.
 * Provides lazy initialization of database and repository instances.
 */
class TaskManagerApplication : Application() {

    val database by lazy { TaskDatabase.getDatabase(this) }
    val repository by lazy {
        TaskRepository(database.taskDao(), database.userProfileDao())
    }
}
