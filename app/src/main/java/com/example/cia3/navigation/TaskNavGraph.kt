package com.example.cia3.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cia3.ui.screens.AddTaskScreen
import com.example.cia3.ui.screens.EditTaskScreen
import com.example.cia3.ui.screens.ManageTasksScreen
import com.example.cia3.ui.screens.TaskListScreen
import com.example.cia3.viewmodel.TaskViewModel

/**
 * Navigation graph for the Task Manager app.
 * Defines all composable destinations and their navigation routes.
 */
@Composable
fun TaskNavGraph(
    navController: NavHostController,
    taskViewModel: TaskViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.TaskList.route
    ) {
        composable(Screen.TaskList.route) {
            TaskListScreen(
                viewModel = taskViewModel,
                onEditTask = { taskId ->
                    navController.navigate(Screen.EditTask.createRoute(taskId))
                }
            )
        }

        composable(Screen.ManageTasks.route) {
            ManageTasksScreen(viewModel = taskViewModel)
        }

        composable(Screen.AddTask.route) {
            AddTaskScreen(
                viewModel = taskViewModel,
                onTaskAdded = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.EditTask.route,
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: return@composable
            EditTaskScreen(
                viewModel = taskViewModel,
                taskId = taskId,
                onTaskUpdated = {
                    navController.popBackStack()
                }
            )
        }
    }
}
