package com.example.travelmemories.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.travelmemories.di.AppContainer
import com.example.travelmemories.ui.screens.ErrorScreen
import com.example.travelmemories.ui.screens.TripDetailScreen
import com.example.travelmemories.ui.screens.TripLibraryScreen
import com.example.travelmemories.ui.state.AppStartupState
import com.example.travelmemories.ui.viewmodel.TripLibraryViewModel

private object Routes {
    const val Library = "library"
    const val TripDetail = "trip/{tripId}"

    fun tripDetail(tripId: Long) = "trip/$tripId"
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
    val viewModel = remember(container) { TripLibraryViewModel(container.tripRepository) }

    NavHost(navController = navController, startDestination = Routes.Library) {
        composable(Routes.Library) {
            TripLibraryScreen(
                viewModel = viewModel,
                onOpenTrip = { tripId ->
                    viewModel.setActiveTrip(tripId)
                    navController.navigate(Routes.tripDetail(tripId))
                }
            )
        }
        composable(
            route = Routes.TripDetail,
            arguments = listOf(navArgument("tripId") { type = NavType.LongType })
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getLong("tripId") ?: return@composable
            TripDetailScreen(
                tripId = tripId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onArchiveTrip = {
                    viewModel.archiveTrip(tripId)
                    navController.popBackStack()
                }
            )
        }
    }
}
