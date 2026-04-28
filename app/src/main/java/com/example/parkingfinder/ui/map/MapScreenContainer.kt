package com.example.parkingfinder.ui.map

import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.parkingfinder.model.ParkingSpace
import com.example.parkingfinder.navigation.Routes
import com.example.parkingfinder.ui.booking.BookingScreen

@Composable
fun MapScreenContainer(
    navController: NavHostController,
    viewModel: MapViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    // When non-null, we show booking screen instead of the map
    var bookingSpace by remember { mutableStateOf<ParkingSpace?>(null) }

    bookingSpace?.let { space ->
        BookingScreen(
            space = space,
            onClose = { bookingSpace = null }
        )
        return
    }

    MapScreen(
        parkingSpaces = state.parkingSpaces,
        selectedParking = state.selectedParking,
        onParkingClicked = viewModel::selectParking,
        onSearchById = viewModel::searchByLocationId
    )

    state.selectedParking?.let { selected ->
        ParkingInfoBottomSheet(
            parkingSpace = selected,
            onDismiss = viewModel::clearSelection,
            onBookClick = { space ->
                viewModel.clearSelection()
                bookingSpace = space
            },
            onMoreInfoClick = { space ->
                viewModel.clearSelection()
                navController.navigate("${Routes.PARKING_DETAILS}/${space.id}")
            }
        )
    }
}