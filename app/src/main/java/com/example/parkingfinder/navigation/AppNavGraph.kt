/*
package com.example.parkingfinder.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.parkingfinder.ui.auth.LoginScreen
import com.example.parkingfinder.ui.auth.SignUpScreen
import com.example.parkingfinder.ui.map.MapScreenContainer
import com.example.parkingfinder.viewmodel.ParkingViewModel

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            LoginScreen(
                onGoToSignUp = {
                    navController.navigate("signup")
                }
            )
        }

        composable("signup") {
            SignUpScreen(
                onGoToLogin = {
                    navController.popBackStack()
                },
                */
/*onSignUpSuccess = {
                    navController.navigate("home") {
                        popUpTo("signup") { inclusive = true }
                    }*//*

                //The following means once user is registered they will need to log in to check account created
                onSignUpSuccess = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
    }
}*/

/*package com.example.parkingfinder.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.parkingfinder.ui.auth.LoginScreen
import com.example.parkingfinder.ui.auth.SignUpScreen

object Routes {
    const val LOGIN = "login"
    const val SIGN_UP = "signup"
    const val MAP = "map"
}

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onGoToSignUp = {
                    navController.navigate(Routes.SIGN_UP)
                },
                onLoginSuccess = {
                    navController.navigate(Routes.MAP) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.SIGN_UP) {
            SignUpScreen(
                onGoToLogin = {
                    navController.popBackStack()
                },
                onSignUpSuccess = {
                    // After signup, force login (your original logic)
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SIGN_UP) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.MAP) {
            com.example.parkingfinder.ui.map.MapScreenContainer()
        }
    }
}*/

package com.example.parkingfinder.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.parkingfinder.ui.auth.LoginScreen
import com.example.parkingfinder.ui.auth.SignUpScreen
import com.example.parkingfinder.ui.auth.ForgotPasswordScreen

import com.example.parkingfinder.ui.account.ProfileRoute
import com.example.parkingfinder.ui.account.SettingsScreen



object Routes {
    const val AUTH_GATE = "auth_gate"
    const val LOGIN = "login"
    const val SIGN_UP = "signup"
    const val MAIN = "main"
    const val PROFILE = "profile"
    const val VEHICLES = "vehicles"
    const val ADD_VEHICLE = "add_vehicles"
    const val VEHICLE_DETAILS = "vehicle_details"
    const val FORGOT_PASSWORD = "forgot_password"
    const val SETTINGS = "settings"
    const val PARKING_DETAILS = "parking_details"
    const val DELETE_ACCOUNT_CONFIRM = "delete_account_confirm"
    const val DELETE_ACCOUNT_SUCCESS = "delete_account_success"

}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()


//    // Check if user is already logged in
//    val currentUser = Firebase.auth.currentUser
//
//    // Start at MAIN if logged in, otherwise LOGIN
//    val startDestination = if (currentUser != null) {
//        Routes.MAIN
//    } else {
//        Routes.LOGIN
//    }

    NavHost(
        navController = navController,
        startDestination = Routes.AUTH_GATE
    ) {

        composable(Routes.AUTH_GATE) {
            AuthGateScreen(navController = navController)
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onGoToSignUp = {
                    navController.navigate(Routes.SIGN_UP)
                },
                onForgotPassword = {
                    navController.navigate(Routes.FORGOT_PASSWORD)
                },
                onLoginSuccess = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        //AppNavGraphy must do the logout function as it is the top-level of the app and can be passed down
        composable(Routes.MAIN) {
            MainAppScreen(
                appNavController = navController,
                onLogout = {
                    navController.navigate(Routes.AUTH_GATE) {
                        popUpTo(Routes.MAIN) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.SIGN_UP) {
            SignUpScreen(
                onGoToLogin = {
                    navController.popBackStack()
                },
                onSignUpSuccess = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SIGN_UP) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onBackToLogin = {
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
    }
}
