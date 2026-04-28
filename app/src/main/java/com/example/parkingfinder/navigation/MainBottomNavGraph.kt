package com.example.parkingfinder.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.parkingfinder.ui.account.AccountScreen
import com.example.parkingfinder.ui.map.MapScreenContainer
import com.example.parkingfinder.ui.session.SessionRoute
import com.google.firebase.auth.FirebaseAuth

import com.example.parkingfinder.ui.session.SessionScreen

import com.example.parkingfinder.ui.vehicle.VehiclesRoute
import com.example.parkingfinder.ui.vehicle.AddVehicleRoute


import androidx.navigation.NavType
import androidx.navigation.navArgument

import com.example.parkingfinder.ui.vehicle.VehicleDetailsRoute

import com.example.parkingfinder.ui.account.ProfileRoute

import com.example.parkingfinder.ui.account.SettingsScreen
import com.example.parkingfinder.ui.map.ParkingDetailsRoute

import com.example.parkingfinder.ui.map.ParkingDetailsScreen

import com.example.parkingfinder.ui.account.DeleteAccountRoute
import com.example.parkingfinder.ui.account.DeleteAccountSuccessScreen

import android.widget.Toast

//this file is for navigation between the session, map, account and help pages

@Composable
fun MainBottomNavGraph(
    navController: NavHostController,
    appNavController: NavHostController,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Map.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Session.route) {
            SessionRoute(
                onBookNewSessionClick = {
                    navController.navigate(BottomNavItem.Map.route){
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(BottomNavItem.Map.route) {
            MapScreenContainer(navController = navController)
        }

        composable(BottomNavItem.Account.route) {
            AccountScreen(
                onLogoutConfirmed = {
                    FirebaseAuth.getInstance().signOut()
                    onLogout()
                },
                onProfileClick = {
                    navController.navigate(Routes.PROFILE)
                },
                onVehiclesClick = {
                    navController.navigate(Routes.VEHICLES)
                },
                onSettingsClick = {
                    navController.navigate(Routes.SETTINGS)
                }
            )
        }

        composable(Routes.PROFILE) {
            ProfileRoute(
                onDeleteAccountClick = {
                    navController.navigate(Routes.DELETE_ACCOUNT_CONFIRM)
                }
            )
        }

        composable(BottomNavItem.Help.route) {
            HelpScreen()
        }

        composable(Routes.VEHICLES) {
            VehiclesRoute(
                onAddVehicleClick = {
                    navController.navigate(Routes.ADD_VEHICLE)
                },
                onVehicleClick = { vehicleId -> navController.navigate("${Routes.VEHICLE_DETAILS}/$vehicleId")
                }
            )
        }

        composable(Routes.ADD_VEHICLE) {
            AddVehicleRoute(
                onVehicleSaved = {
                    //will return the driver to the vehicles page after new reg is saved
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "${Routes.VEHICLE_DETAILS}/{vehicleId}",
            arguments = listOf(
                navArgument("vehicleId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getString("vehicleId").orEmpty()

            VehicleDetailsRoute(
                vehicleId = vehicleId,
                onBackClick = {
                    navController.popBackStack()
                },
                onVehicleDeleted = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.DELETE_ACCOUNT_CONFIRM) {
            DeleteAccountRoute(
                onBackClick = {
                    navController.popBackStack()
                },
                onDeleteSuccess = {
                    navController.navigate(Routes.DELETE_ACCOUNT_SUCCESS) {
                        popUpTo(Routes.PROFILE) { inclusive = true }
                    }
                },
                onDeleteFailure = {
                    // the delete failure is kept quiet, later consider a snackbar or toast
                }
            )
        }

        composable(Routes.DELETE_ACCOUNT_SUCCESS) {
            DeleteAccountSuccessScreen(
                onFinished = {
                    Toast.makeText(
                        navController.context,
                        "Account deleted successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    onLogout()
                }
            )
        }

        composable(
            route = "${Routes.PARKING_DETAILS}/{parkingId}",
            arguments = listOf(
                navArgument("parkingId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val parkingId = backStackEntry.arguments?.getString("parkingId").orEmpty()

            ParkingDetailsRoute(
                parkingId = parkingId,
                onBackClick = {
                    navController.popBackStack()
                },
                onBookClick = {
                    // placeholder for your booking flow
                }
            )
        }

    }
}