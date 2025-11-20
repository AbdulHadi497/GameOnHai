package com.project.gameonhai.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.project.gameonhai.feature_court.presentation.CourtDetailsScreen
import com.project.gameonhai.feature_court.presentation.GameSelectionScreen
import com.project.gameonhai.feature_court.presentation.TimeSlotScreen
import com.project.gameonhai.feature_home.presentation.HomeScreen

/**
 * Sealed class defining all navigation routes in the app
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CourtDetails : Screen("court_details/{courtId}") {
        fun createRoute(courtId: String) = "court_details/$courtId"
    }
    object GameSelection : Screen("game_selection/{courtId}/{courtName}") {
        fun createRoute(courtId: String, courtName: String) =
            "game_selection/$courtId/$courtName"
    }
    object TimeSlot : Screen("time_slot/{courtId}/{gameId}/{gameName}") {
        fun createRoute(courtId: String, gameId: String, gameName: String) =
            "time_slot/$courtId/$gameId/$gameName"
    }
    // Future screens
    object BookingForm : Screen("booking_form/{courtId}/{gameId}/{timeSlotId}") {
        fun createRoute(courtId: String, gameId: String, timeSlotId: String) =
            "booking_form/$courtId/$gameId/$timeSlotId"
    }
    object BookingConfirmation : Screen("booking_confirmation/{bookingId}") {
        fun createRoute(bookingId: String) = "booking_confirmation/$bookingId"
    }
    object MyBookings : Screen("my_bookings")
}

/**
 * Main navigation graph for the entire app
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Home Screen
        composable(route = Screen.Home.route) {
            HomeScreen(
                onCourtClick = { courtId ->
                    navController.navigate(Screen.CourtDetails.createRoute(courtId))
                }
            )
        }

        // Court Details Screen
        composable(
            route = Screen.CourtDetails.route,
            arguments = listOf(
                navArgument("courtId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val courtId = backStackEntry.arguments?.getString("courtId") ?: return@composable

            CourtDetailsScreen(
                courtId = courtId,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToGameSelection = { cId, courtName ->
                    navController.navigate(Screen.GameSelection.createRoute(cId, courtName))
                }
            )
        }

        // Game Selection Screen
        composable(
            route = Screen.GameSelection.route,
            arguments = listOf(
                navArgument("courtId") { type = NavType.StringType },
                navArgument("courtName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val courtId = backStackEntry.arguments?.getString("courtId") ?: return@composable
            val courtName = backStackEntry.arguments?.getString("courtName") ?: return@composable

            GameSelectionScreen(
                courtId = courtId,
                courtName = courtName,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToTimeSlots = { cId, gameId, gameName ->
                    navController.navigate(Screen.TimeSlot.createRoute(cId, gameId, gameName))
                }
            )
        }

        // Time Slot Screen
        composable(
            route = Screen.TimeSlot.route,
            arguments = listOf(
                navArgument("courtId") { type = NavType.StringType },
                navArgument("gameId") { type = NavType.StringType },
                navArgument("gameName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val courtId = backStackEntry.arguments?.getString("courtId") ?: return@composable
            val gameId = backStackEntry.arguments?.getString("gameId") ?: return@composable
            val gameName = backStackEntry.arguments?.getString("gameName") ?: return@composable

            TimeSlotScreen(
                courtId = courtId,
                gameId = gameId,
                gameName = gameName,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToBooking = { cId, gId, timeSlotId ->
                    // TODO: Navigate to booking form when implemented
                    // navController.navigate(Screen.BookingForm.createRoute(cId, gId, timeSlotId))
                }
            )
        }

        // TODO: Add BookingForm, BookingConfirmation, MyBookings screens
    }
}