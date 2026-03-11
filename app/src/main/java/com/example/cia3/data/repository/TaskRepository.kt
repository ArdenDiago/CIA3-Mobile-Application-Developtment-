package com.example.cia3.data.repository

import com.example.cia3.data.dao.TaskDao
import com.example.cia3.data.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Repository class that acts as an intermediary between the DAO and ViewModel.
 * This is the Repository layer of the MVVM architecture.
 */
class TaskRepository(private val taskDao: TaskDao) {

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
}
