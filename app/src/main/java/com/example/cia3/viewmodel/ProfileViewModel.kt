package com.example.cia3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cia3.data.model.DailyCompletionCount
import com.example.cia3.data.model.MonthlyStats
import com.example.cia3.data.model.Task
import com.example.cia3.data.model.UserProfile
import com.example.cia3.data.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * ViewModel for the Profile screen.
 * Manages user profile data and productivity analytics.
 */
class ProfileViewModel(private val repository: TaskRepository) : ViewModel() {

    val userProfile: Flow<UserProfile?> = repository.userProfile
    val totalCompletedCount: Flow<Int> = repository.totalCompletedCount
    val dailyCompletionCounts: Flow<List<DailyCompletionCount>> = repository.dailyCompletionCounts

    private val _selectedDate = MutableStateFlow<String?>(null)
    val selectedDate: StateFlow<String?> = _selectedDate.asStateFlow()

    private val _selectedDateTasks = MutableStateFlow<List<Task>>(emptyList())
    val selectedDateTasks: StateFlow<List<Task>> = _selectedDateTasks.asStateFlow()

    private val _showDayDetail = MutableStateFlow(false)
    val showDayDetail: StateFlow<Boolean> = _showDayDetail.asStateFlow()

    val currentStreak: StateFlow<Int> = dailyCompletionCounts
        .combine(MutableStateFlow(Unit)) { counts, _ ->
            calculateCurrentStreak(counts)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    init {
        ensureProfileExists()
    }

    private fun ensureProfileExists() {
        viewModelScope.launch {
            repository.userProfile.collect { profile ->
                if (profile == null) {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    repository.insertProfile(
                        UserProfile(
                            name = "User",
                            email = "user@example.com",
                            joinDate = dateFormat.format(Date())
                        )
                    )
                }
                return@collect
            }
        }
    }

    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.updateProfile(profile)
        }
    }

    fun onDateClicked(date: String) {
        _selectedDate.value = date
        _showDayDetail.value = true
        viewModelScope.launch {
            repository.getTasksByCompletedDate(date).collect { tasks ->
                _selectedDateTasks.value = tasks
            }
        }
    }

    fun dismissDayDetail() {
        _showDayDetail.value = false
        _selectedDate.value = null
    }

    fun getMonthlyStats(year: Int, month: Int): Flow<MonthlyStats> {
        val monthPrefix = String.format(Locale.getDefault(), "%04d-%02d", year, month)
        val monthNames = arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )

        return combine(
            repository.getCompletedCountForMonth(monthPrefix),
            repository.getTotalTasksForMonth(monthPrefix),
            repository.getCompletionCountsForMonth(monthPrefix)
        ) { completed, total, dailyCounts ->
            val mostProductive = dailyCounts.maxByOrNull { it.count }
            val longestStreak = calculateLongestStreakInMonth(dailyCounts, year, month)
            val percentage = if (total > 0) (completed.toFloat() / total * 100f) else 0f

            MonthlyStats(
                month = monthNames[month - 1],
                year = year,
                totalCompleted = completed,
                mostProductiveDay = mostProductive?.completedDate,
                mostProductiveDayCount = mostProductive?.count ?: 0,
                longestStreak = longestStreak,
                totalTasks = total,
                completionPercentage = percentage
            )
        }
    }

    private fun calculateCurrentStreak(counts: List<DailyCompletionCount>): Int {
        if (counts.isEmpty()) return 0

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

        val completedDates = counts.mapNotNull { count ->
            try {
                val cal = Calendar.getInstance()
                cal.time = dateFormat.parse(count.completedDate) ?: return@mapNotNull null
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                cal.timeInMillis
            } catch (e: Exception) {
                null
            }
        }.toSet()

        var streak = 0
        val checkDate = today.clone() as Calendar

        // Check if today has completions, if not start from yesterday
        if (!completedDates.contains(checkDate.timeInMillis)) {
            checkDate.add(Calendar.DAY_OF_YEAR, -1)
            if (!completedDates.contains(checkDate.timeInMillis)) {
                return 0
            }
        }

        while (completedDates.contains(checkDate.timeInMillis)) {
            streak++
            checkDate.add(Calendar.DAY_OF_YEAR, -1)
        }

        return streak
    }

    private fun calculateLongestStreakInMonth(
        counts: List<DailyCompletionCount>,
        year: Int,
        month: Int
    ): Int {
        if (counts.isEmpty()) return 0

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val completedDates = counts.mapNotNull { count ->
            try {
                val cal = Calendar.getInstance()
                cal.time = dateFormat.parse(count.completedDate) ?: return@mapNotNull null
                cal.get(Calendar.DAY_OF_MONTH)
            } catch (e: Exception) {
                null
            }
        }.toSet()

        val cal = Calendar.getInstance()
        cal.set(year, month - 1, 1)
        val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        var longestStreak = 0
        var currentStreak = 0

        for (day in 1..daysInMonth) {
            if (completedDates.contains(day)) {
                currentStreak++
                longestStreak = maxOf(longestStreak, currentStreak)
            } else {
                currentStreak = 0
            }
        }

        return longestStreak
    }

    /**
     * Returns the heatmap color level (0-4) based on completion count.
     * 0 = no tasks, 1 = few, 2 = moderate, 3 = many, 4 = very productive
     */
    companion object {
        fun getHeatmapLevel(count: Int): Int {
            return when {
                count == 0 -> 0
                count <= 1 -> 1
                count <= 3 -> 2
                count <= 5 -> 3
                else -> 4
            }
        }
    }
}

/**
 * Factory for creating ProfileViewModel with repository dependency.
 */
class ProfileViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
