package com.example.cia3.data.repository

import com.example.cia3.data.dao.TaskDao
import com.example.cia3.data.dao.UserProfileDao
import com.example.cia3.data.model.DailyCompletionCount
import com.example.cia3.data.model.Task
import com.example.cia3.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

/**
 * Repository class that acts as an intermediary between the DAOs and ViewModels.
 * This is the Repository layer of the MVVM architecture.
 */
class TaskRepository(
    private val taskDao: TaskDao,
    private val userProfileDao: UserProfileDao
) {

    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    fun getTaskById(taskId: Int): Flow<Task> {
        return taskDao.getTaskById(taskId)
    }

    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun deleteAllTasks() {
        taskDao.deleteAllTasks()
    }

    // Profile operations
    val userProfile: Flow<UserProfile?> = userProfileDao.getProfile()

    suspend fun insertProfile(profile: UserProfile) {
        userProfileDao.insertProfile(profile)
    }

    suspend fun updateProfile(profile: UserProfile) {
        userProfileDao.updateProfile(profile)
    }

    // Analytics operations
    val totalCompletedCount: Flow<Int> = taskDao.getTotalCompletedCount()

    val dailyCompletionCounts: Flow<List<DailyCompletionCount>> =
        taskDao.getDailyCompletionCounts()

    fun getTasksByCompletedDate(date: String): Flow<List<Task>> {
        return taskDao.getTasksByCompletedDate(date)
    }

    fun getCompletionCountsForMonth(monthPrefix: String): Flow<List<DailyCompletionCount>> {
        return taskDao.getCompletionCountsForMonth(monthPrefix)
    }

    fun getCompletedCountForMonth(monthPrefix: String): Flow<Int> {
        return taskDao.getCompletedCountForMonth(monthPrefix)
    }

    fun getTotalTasksForMonth(monthPrefix: String): Flow<Int> {
        return taskDao.getTotalTasksForMonth(monthPrefix)
    }
}
