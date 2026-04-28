package com.example.parkingfinder.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.parkingfinder.viewmodel.AuthViewModel



// This page will see the auth state form AuthViewModel, while it is checking the auth state it will show a loading sign
//If the user is signed in it will send them to the main page, if not signed in it will send them to login page

@Composable
fun AuthGateScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            AuthState.Authenticated -> {
                navController.navigate(Routes.MAIN) {
                    popUpTo(Routes.AUTH_GATE) { inclusive = true }
                    launchSingleTop = true
                }
            }

            AuthState.Unauthenticated -> {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.AUTH_GATE) { inclusive = true }
                    launchSingleTop = true
                }
            }

            AuthState.Loading -> {
                // Stay on this screen while auth is being checked
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Text(
            text = "Checking session...",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}