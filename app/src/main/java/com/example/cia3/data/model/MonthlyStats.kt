package com.example.cia3.data.model

/**
 * Data class representing monthly productivity statistics.
 */
data class MonthlyStats(
    val month: String,
    val year: Int,
    val totalCompleted: Int,
    val mostProductiveDay: String?,
    val mostProductiveDayCount: Int,
    val longestStreak: Int,
    val totalTasks: Int,
    val completionPercentage: Float
)
