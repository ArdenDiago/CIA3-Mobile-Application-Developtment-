package com.example.cia3.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cia3.ui.screens.AddTaskScreen
import com.example.cia3.ui.screens.EditTaskScreen
import com.example.cia3.ui.screens.LoginScreen
import com.example.cia3.ui.screens.ManageTasksScreen
import com.example.cia3.ui.screens.ProfileScreen
import com.example.cia3.ui.screens.SplashScreen
import com.example.cia3.ui.screens.TaskListScreen
import com.example.cia3.viewmodel.ProfileViewModel
import com.example.cia3.viewmodel.TaskViewModel

/**
 * Navigation graph for the FocusBoard app.
 * Flow: Splash -> (check profile) -> Login or TaskList
 */
@Composable
fun TaskNavGraph(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    profileViewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                userProfileFlow = profileViewModel.userProfile,
                onNavigateToHome = {
                    navController.navigate(Screen.TaskList.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                profileViewModel = profileViewModel,
                onLoginComplete = {
                    navController.navigate(Screen.TaskList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

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

        composable(Screen.Profile.route) {
            ProfileScreen(profileViewModel = profileViewModel)
        }
    }
}
