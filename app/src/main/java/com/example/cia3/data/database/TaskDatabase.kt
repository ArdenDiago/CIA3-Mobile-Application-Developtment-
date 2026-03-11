package com.example.cia3.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cia3.data.dao.TaskDao
import com.example.cia3.data.dao.UserProfileDao
import com.example.cia3.data.model.Task
import com.example.cia3.data.model.UserProfile

/**
 * Room Database class for the Task Manager application.
 * Singleton pattern ensures only one database instance exists.
 */
@Database(
    entities = [Task::class, UserProfile::class],
    version = 2,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
