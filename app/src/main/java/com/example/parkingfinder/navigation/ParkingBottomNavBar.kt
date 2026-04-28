package com.example.parkingfinder.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun ParkingBottomNavBar(
    navController: NavHostController
) {
    val items = listOf(
        BottomNavItem.Session,
        BottomNavItem.Map,
        BottomNavItem.Account,
        BottomNavItem.Help
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = Modifier
            .height(80.dp)
            .padding(bottom = 6.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        items.forEach { item ->
            val selected = isBottomNavItemSelected(
                itemRoute = item.route,
                currentRoute = currentRoute
            )

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(item.route) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                        fontSize = 13.sp
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSurface,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    indicatorColor = MaterialTheme.colorScheme.surface,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

private fun isBottomNavItemSelected(
    itemRoute: String,
    currentRoute: String?
): Boolean {
    return when (itemRoute) {
        BottomNavItem.Account.route -> {
            currentRoute == BottomNavItem.Account.route ||
                    currentRoute == Routes.PROFILE ||
                    currentRoute == Routes.VEHICLES ||
                    currentRoute == Routes.ADD_VEHICLE ||
                    currentRoute?.startsWith(Routes.VEHICLE_DETAILS) == true
        }

        else -> currentRoute == itemRoute
    }
}