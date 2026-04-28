package com.example.parkingfinder.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ParkingDetailsRoute(
    parkingId: String,
    onBackClick: () -> Unit,
    onBookClick: () -> Unit
) {
    val viewModel: MapViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

    val selectedParking = state.parkingSpaces.find { it.id == parkingId }

    ParkingDetailsScreen(
        parkingSpace = selectedParking,
        onBackClick = onBackClick,
        onBookClick = {
            onBookClick()
        }
    )
}