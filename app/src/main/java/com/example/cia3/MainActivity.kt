package com.example.cia3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cia3.navigation.Screen
import com.example.cia3.navigation.TaskNavGraph
import com.example.cia3.ui.components.TaskBottomNavigationBar
import com.example.cia3.ui.components.TaskTopAppBar
import com.example.cia3.ui.theme.CIA3Theme
import com.example.cia3.viewmodel.TaskViewModel
import com.example.cia3.viewmodel.TaskViewModelFactory

/**
 * Main Activity - Entry point of the Task Manager application.
 * Sets up the full app scaffold with TopAppBar, BottomNavigation, FAB, and NavHost.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CIA3Theme {
                val application = application as TaskManagerApplication
                val viewModel: TaskViewModel = viewModel(
                    factory = TaskViewModelFactory(application.repository)
                )
                TaskManagerApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun TaskManagerApp(viewModel: TaskViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Determine if FAB and bottom nav should be visible
    val showBottomBar = currentRoute in listOf(
        Screen.TaskList.route,
        Screen.ManageTasks.route
    )
    val showFab = currentRoute == Screen.TaskList.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TaskTopAppBar(
                onDeleteAllTasks = { viewModel.deleteAllTasks() }
            )
        },
        bottomBar = {
            if (showBottomBar) {
                TaskBottomNavigationBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.TaskList.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Screen.AddTask.route)
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Task"
                    )
                }
            }
        }
    ) { innerPadding ->
        TaskNavGraph(
            navController = navController,
            taskViewModel = viewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}