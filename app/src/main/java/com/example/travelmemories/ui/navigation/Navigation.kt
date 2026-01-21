package com.example.travelmemories.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.travelmemories.di.AppContainer
import com.example.travelmemories.ui.screens.ErrorScreen
import com.example.travelmemories.ui.screens.HomeScreen
import com.example.travelmemories.ui.screens.PlaceholderScreen
import com.example.travelmemories.ui.state.AppStartupState
import com.example.travelmemories.ui.viewmodel.MainViewModel

private object Routes {
    const val Home = "home"
    const val Placeholder = "placeholder"
}

@Composable
fun TravelMemoriesNavHost(startupState: AppStartupState) {
    when (startupState) {
        is AppStartupState.Ready -> AppNavHost(container = startupState.container)
        is AppStartupState.Failed -> ErrorScreen(message = startupState.message)
    }
}

@Composable
private fun AppNavHost(container: AppContainer) {
    val navController = rememberNavController()
    val viewModel = remember(container) { MainViewModel(container.mediaSmokeTestRepository) }

    NavHost(navController = navController, startDestination = Routes.Home) {
        composable(Routes.Home) {
            HomeScreen(
                viewModel = viewModel,
                onNavigatePlaceholder = { navController.navigate(Routes.Placeholder) }
            )
        }
        composable(Routes.Placeholder) {
            PlaceholderScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
