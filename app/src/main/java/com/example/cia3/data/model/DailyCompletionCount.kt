package com.example.cia3.data.model

/**
 * Data class representing the number of tasks completed on a specific date.
 * Used for generating the productivity heatmap.
 */
data class DailyCompletionCount(
    val completedDate: String,
    val count: Int
)
