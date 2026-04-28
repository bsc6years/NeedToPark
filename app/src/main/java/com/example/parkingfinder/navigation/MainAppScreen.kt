package com.example.parkingfinder.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.api.Control

/*
Explained from CHATGPT:
AppNavGraph handles top-level routes like login and main app
MainAppScreen holds the Scaffold + bottom nav bar
MainBottomNavGraph controls the screens that should appear with the bottom nav still visible*/


//The main app screen makes use of a nested nav controller
@Composable
fun MainAppScreen(
    appNavController: NavHostController,
    onLogout: () -> Unit
) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            ParkingBottomNavBar(navController = bottomNavController)
            //The above controller knows about the bottom tabs inside the app
            //but does not know how to do top level login and auth routes. only navcontroller in appnavgraph knows that
        }
    ) { innerPadding ->
        MainBottomNavGraph(
            navController = bottomNavController,
            appNavController = appNavController,
            modifier = Modifier.padding(innerPadding),
            onLogout = onLogout
        )
    }
}

