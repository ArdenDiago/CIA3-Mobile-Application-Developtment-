package com.example.cia3.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.cia3.data.model.DailyCompletionCount
import com.example.cia3.data.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Task entity.
 * Provides CRUD operations and analytics queries for the tasks table.
 */
@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Query("SELECT * FROM tasks ORDER BY taskId DESC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE taskId = :taskId")
    fun getTaskById(taskId: Int): Flow<Task>

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()

    // Analytics queries

    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 1")
    fun getTotalCompletedCount(): Flow<Int>

    @Query(
        "SELECT completedDate, COUNT(*) as count FROM tasks " +
        "WHERE isCompleted = 1 AND completedDate IS NOT NULL " +
        "GROUP BY completedDate ORDER BY completedDate ASC"
    )
    fun getDailyCompletionCounts(): Flow<List<DailyCompletionCount>>

    @Query(
        "SELECT * FROM tasks WHERE completedDate = :date ORDER BY taskId DESC"
    )
    fun getTasksByCompletedDate(date: String): Flow<List<Task>>

    @Query(
        "SELECT completedDate, COUNT(*) as count FROM tasks " +
        "WHERE isCompleted = 1 AND completedDate IS NOT NULL " +
        "AND completedDate LIKE :monthPrefix || '%' " +
        "GROUP BY completedDate ORDER BY completedDate ASC"
    )
    fun getCompletionCountsForMonth(monthPrefix: String): Flow<List<DailyCompletionCount>>

    @Query(
        "SELECT COUNT(*) FROM tasks WHERE completedDate LIKE :monthPrefix || '%' AND isCompleted = 1"
    )
    fun getCompletedCountForMonth(monthPrefix: String): Flow<Int>

    @Query(
        "SELECT COUNT(*) FROM tasks WHERE dueDate LIKE :monthPrefix || '%'"
    )
    fun getTotalTasksForMonth(monthPrefix: String): Flow<Int>
}
