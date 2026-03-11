package com.example.cia3.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cia3.data.model.Task
import com.example.cia3.ui.components.DateTimePickerField
import com.example.cia3.viewmodel.TaskViewModel

/**
 * Screen for editing an existing task.
 * Pre-fills fields with existing task data and updates via ViewModel.
 */
@Composable
fun EditTaskScreen(
    viewModel: TaskViewModel,
    taskId: Int,
    onTaskUpdated: () -> Unit
) {
    val task by viewModel.getTaskById(taskId).collectAsState(initial = null)

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var titleError by remember { mutableStateOf(false) }
    var dueDateError by remember { mutableStateOf(false) }
    var isLoaded by remember { mutableStateOf(false) }

    // Pre-fill fields when task is loaded
    LaunchedEffect(task) {
        task?.let {
            if (!isLoaded) {
                title = it.title
                description = it.description
                dueDate = it.dueDate
                isLoaded = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Edit Task",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
                titleError = false
            },
            label = { Text("Task Title") },
            placeholder = { Text("Enter task title") },
            leadingIcon = {
                Icon(Icons.Default.Title, contentDescription = "Title")
            },
            isError = titleError,
            supportingText = {
                if (titleError) Text("Title is required")
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            placeholder = { Text("Enter task description") },
            leadingIcon = {
                Icon(Icons.Default.Description, contentDescription = "Description")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            maxLines = 4
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Calendar date & time picker
        DateTimePickerField(
            dateTimeValue = dueDate,
            onDateTimeSelected = { selected ->
                dueDate = selected
                dueDateError = false
            },
            label = "Due Date"
        )

        if (dueDateError) {
            Text(
                text = "Please select a due date",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val hasError = title.isBlank()
                titleError = hasError
                dueDateError = dueDate.isBlank()

                if (!hasError && dueDate.isNotBlank()) {
                    task?.let {
                        viewModel.updateTask(
                            Task(
                                taskId = it.taskId,
                                title = title.trim(),
                                description = description.trim(),
                                dueDate = dueDate.trim(),
                                isCompleted = it.isCompleted,
                                completedDate = it.completedDate
                            )
                        )
                        onTaskUpdated()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Update Task",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
