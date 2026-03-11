package com.example.cia3.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * A date & time picker field that opens a Material3 calendar DatePicker on tap.
 * Includes a toggle chip to optionally add a time selection.
 *
 * @param dateTimeValue The current date/time string displayed (yyyy-MM-dd or yyyy-MM-dd HH:mm)
 * @param onDateTimeSelected Callback with the selected date/time string
 * @param label Label text for the field
 * @param modifier Modifier
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerField(
    dateTimeValue: String,
    onDateTimeSelected: (String) -> Unit,
    label: String = "Due Date",
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var includeTime by remember { mutableStateOf(dateTimeValue.contains(":")) }
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    var selectedHour by remember { mutableStateOf(9) }
    var selectedMinute by remember { mutableStateOf(0) }

    // Initialize from existing value
    remember(dateTimeValue) {
        if (dateTimeValue.isNotBlank()) {
            try {
                if (dateTimeValue.contains(":")) {
                    includeTime = true
                    val parts = dateTimeValue.split(" ")
                    if (parts.size == 2) {
                        val timeParts = parts[1].split(":")
                        selectedHour = timeParts[0].toIntOrNull() ?: 9
                        selectedMinute = timeParts[1].toIntOrNull() ?: 0
                    }
                }
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                dateFormat.timeZone = TimeZone.getDefault()
                val date = dateFormat.parse(dateTimeValue.split(" ")[0])
                selectedDateMillis = date?.time
            } catch (_: Exception) { }
        }
        true
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Read-only text field that opens the date picker on tap
        OutlinedTextField(
            value = dateTimeValue,
            onValueChange = { },
            label = { Text(label) },
            placeholder = { Text("Tap to select date") },
            leadingIcon = {
                Icon(Icons.Default.CalendarMonth, contentDescription = "Calendar")
            },
            trailingIcon = {
                if (includeTime && dateTimeValue.isNotBlank()) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = "Time included",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            readOnly = true,
            enabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.primary,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            singleLine = true,
            interactionSource = remember { NoRippleInteractionSource { showDatePicker = true } }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Toggle chip for including time
        FilterChip(
            selected = includeTime,
            onClick = {
                includeTime = !includeTime
                if (!includeTime && dateTimeValue.contains(":")) {
                    // Remove time portion
                    val dateOnly = dateTimeValue.split(" ")[0]
                    onDateTimeSelected(dateOnly)
                } else if (includeTime && dateTimeValue.isNotBlank() && !dateTimeValue.contains(":")) {
                    // Add default time
                    val timeStr = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
                    onDateTimeSelected("${dateTimeValue.split(" ")[0]} $timeStr")
                }
            },
            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = null,
                        modifier = Modifier.height(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Include Time")
                }
            },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
    }

    // Date picker dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDateMillis ?: System.currentTimeMillis()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            selectedDateMillis = millis
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
                            val dateStr = dateFormat.format(Date(millis))

                            if (includeTime) {
                                showDatePicker = false
                                showTimePicker = true
                            } else {
                                onDateTimeSelected(dateStr)
                                showDatePicker = false
                            }
                        }
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Time picker dialog
    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = selectedHour,
            initialMinute = selectedMinute,
            is24Hour = false
        )

        Dialog(onDismissRequest = { showTimePicker = false }) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Select Time",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                    TimePicker(
                        state = timePickerState,
                        colors = TimePickerDefaults.colors(
                            clockDialColor = MaterialTheme.colorScheme.surfaceVariant,
                            selectorColor = MaterialTheme.colorScheme.primary,
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showTimePicker = false }) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(
                            onClick = {
                                selectedHour = timePickerState.hour
                                selectedMinute = timePickerState.minute
                                selectedDateMillis?.let { millis ->
                                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
                                    val dateStr = dateFormat.format(Date(millis))
                                    val timeStr = String.format(
                                        Locale.getDefault(),
                                        "%02d:%02d",
                                        selectedHour,
                                        selectedMinute
                                    )
                                    onDateTimeSelected("$dateStr $timeStr")
                                }
                                showTimePicker = false
                            }
                        ) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }
}

/**
 * Custom InteractionSource that triggers a callback on press (to open the date picker
 * when the read-only text field is tapped).
 */
private class NoRippleInteractionSource(
    private val onPress: () -> Unit
) : androidx.compose.foundation.interaction.MutableInteractionSource {

    override val interactions = kotlinx.coroutines.flow.MutableSharedFlow<androidx.compose.foundation.interaction.Interaction>()

    override suspend fun emit(interaction: androidx.compose.foundation.interaction.Interaction) {
        if (interaction is androidx.compose.foundation.interaction.PressInteraction.Press) {
            onPress()
        }
        interactions.emit(interaction)
    }

    override fun tryEmit(interaction: androidx.compose.foundation.interaction.Interaction): Boolean {
        if (interaction is androidx.compose.foundation.interaction.PressInteraction.Press) {
            onPress()
        }
        return interactions.tryEmit(interaction)
    }
}
