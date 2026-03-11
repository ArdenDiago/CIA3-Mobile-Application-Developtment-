package com.example.cia3.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cia3.data.model.DailyCompletionCount
import com.example.cia3.viewmodel.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Heatmap color palette (GitHub-style greens)
private val HeatmapLevel0Light = Color(0xFFEBEDF0)
private val HeatmapLevel1 = Color(0xFF9BE9A8)
private val HeatmapLevel2 = Color(0xFF40C463)
private val HeatmapLevel3 = Color(0xFF30A14E)
private val HeatmapLevel4 = Color(0xFF216E39)
private val HeatmapLevel0Dark = Color(0xFF2D333B)

/**
 * GitHub-style contribution heatmap calendar.
 * Displays the last 365 days of task completion activity.
 */
@Composable
fun HeatmapCalendar(
    completionCounts: List<DailyCompletionCount>,
    onDateClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDarkTheme = !MaterialTheme.colorScheme.background.luminance().let { it > 0.5f }
    val emptyColor = if (isDarkTheme) HeatmapLevel0Dark else HeatmapLevel0Light

    // Build a map of date -> count for quick lookup
    val countMap = remember(completionCounts) {
        completionCounts.associate { it.completedDate to it.count }
    }

    // Generate weeks data for the last 52 weeks
    val weeksData = remember(countMap) {
        generateWeeksData(countMap)
    }

    val monthLabels = remember(weeksData) {
        generateMonthLabels(weeksData)
    }

    val dayLabels = listOf("", "Mon", "", "Wed", "", "Fri", "")

    Column(modifier = modifier.padding(horizontal = 4.dp)) {
        Text(
            text = "Productivity Heatmap",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row {
            // Day labels column
            Column(
                modifier = Modifier.padding(end = 4.dp, top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                dayLabels.forEach { label ->
                    Box(
                        modifier = Modifier.size(14.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = label,
                            fontSize = 8.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.End
                        )
                    }
                }
            }

            // Scrollable heatmap grid
            Column(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState(Int.MAX_VALUE))
            ) {
                // Month labels row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(0.dp),
                    modifier = Modifier.height(16.dp)
                ) {
                    monthLabels.forEach { (label, weekIndex) ->
                        if (label.isNotEmpty()) {
                            Box(modifier = Modifier.width((weekIndex * 16).dp)) // offset
                            Text(
                                text = label,
                                fontSize = 8.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Heatmap grid (7 rows x 52 columns)
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    weeksData.forEach { week ->
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            week.forEach { dayData ->
                                val level = ProfileViewModel.getHeatmapLevel(dayData.count)
                                val color = getHeatmapColor(level, emptyColor)
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(color)
                                        .then(
                                            if (dayData.date.isNotEmpty()) {
                                                Modifier.clickable {
                                                    onDateClick(dayData.date)
                                                }
                                            } else Modifier
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Legend
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.padding(start = 20.dp)
        ) {
            Text(
                text = "Less",
                fontSize = 9.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(4.dp))
            listOf(emptyColor, HeatmapLevel1, HeatmapLevel2, HeatmapLevel3, HeatmapLevel4)
                .forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(color)
                            .border(
                                0.5.dp,
                                MaterialTheme.colorScheme.outlineVariant,
                                RoundedCornerShape(2.dp)
                            )
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                }
            Text(
                text = "More",
                fontSize = 9.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun getHeatmapColor(level: Int, emptyColor: Color): Color {
    return when (level) {
        0 -> emptyColor
        1 -> HeatmapLevel1
        2 -> HeatmapLevel2
        3 -> HeatmapLevel3
        4 -> HeatmapLevel4
        else -> emptyColor
    }
}

/**
 * Represents a single day cell in the heatmap.
 */
data class HeatmapDay(
    val date: String,
    val count: Int,
    val dayOfWeek: Int
)

/**
 * Generates 52 weeks of heatmap data ending today.
 */
private fun generateWeeksData(countMap: Map<String, Int>): List<List<HeatmapDay>> {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val calendar = Calendar.getInstance()

    // Go back to the start: 52 weeks ago, aligned to Sunday
    calendar.add(Calendar.WEEK_OF_YEAR, -52)
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

    val weeks = mutableListOf<List<HeatmapDay>>()
    val today = Calendar.getInstance()

    for (week in 0 until 53) {
        val days = mutableListOf<HeatmapDay>()
        for (day in 0 until 7) {
            val dateStr = dateFormat.format(calendar.time)
            val isFuture = calendar.after(today)
            days.add(
                HeatmapDay(
                    date = if (isFuture) "" else dateStr,
                    count = if (isFuture) 0 else (countMap[dateStr] ?: 0),
                    dayOfWeek = day
                )
            )
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        weeks.add(days)
    }

    return weeks
}

/**
 * Generates month labels positioned at the correct week columns.
 */
private fun generateMonthLabels(weeks: List<List<HeatmapDay>>): List<Pair<String, Int>> {
    val monthNames = arrayOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val labels = mutableListOf<Pair<String, Int>>()
    var lastMonth = -1

    weeks.forEachIndexed { index, week ->
        val firstDayDate = week.firstOrNull { it.date.isNotEmpty() }?.date ?: return@forEachIndexed
        try {
            val cal = Calendar.getInstance()
            cal.time = dateFormat.parse(firstDayDate) ?: return@forEachIndexed
            val month = cal.get(Calendar.MONTH)
            if (month != lastMonth) {
                labels.add(monthNames[month] to index)
                lastMonth = month
            }
        } catch (_: Exception) {}
    }

    return labels
}

/**
 * Extension to get luminance approximation for a Color.
 */
private fun Color.luminance(): Float {
    return (0.299f * red + 0.587f * green + 0.114f * blue)
}
