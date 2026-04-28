package com.example.parkingfinder.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Session : BottomNavItem(
        route = "session",
        label = "Session",
        selectedIcon = Icons.Filled.Schedule,
        unselectedIcon = Icons.Outlined.Schedule
    )

    data object Map : BottomNavItem(
        route = "map",
        label = "Map",
        selectedIcon = Icons.Filled.Map,
        unselectedIcon = Icons.Outlined.Map
    )

    data object Account : BottomNavItem(
        route = "account",
        label = "Account",
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle
    )

    data object Help : BottomNavItem(
        route = "help",
        label = "Help",
        selectedIcon = Icons.Filled.Help,
        unselectedIcon = Icons.Outlined.Help
    )
}