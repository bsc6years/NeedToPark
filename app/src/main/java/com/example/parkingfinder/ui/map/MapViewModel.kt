package com.example.parkingfinder.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingfinder.data.ParkingRepository
import com.example.parkingfinder.model.ParkingSpace
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MapUiState(
    val parkingSpaces: List<ParkingSpace> = emptyList(),
    val selectedParking: ParkingSpace? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class MapViewModel @Inject constructor(private val repo: ParkingRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState(isLoading = true))
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    // Stores the full list from Firebase (source of truth)
    private var allSpaces: List<ParkingSpace> = emptyList()

    /*    // This is dummy code to check a marker appears on the map
        //Keep this code to refer to in report
        init {
            _uiState.value = _uiState.value.copy(
                parkingSpaces = listOf(
                    ParkingSpace(
                        id = "test",
                        name = "Test Space",
                        latitude = 55.53629,
                        longitude = -0.15706,
                        pricePerHour = 3.5,
                        size = "Medium",
                        rating = 4.6
                    )
                )
            )
        }*/

    init {
        viewModelScope.launch {
            try {
                repo.observeParkingSpaces().collect { spaces ->
                    // ✅ OPTION A: Always refresh from Firebase and show ALL markers.
                    // This ensures new parking spots added in Firebase always appear in the app.
                    allSpaces = spaces

                    _uiState.value = _uiState.value.copy(
                        parkingSpaces = spaces, // ✅ always show all markers
                        isLoading = false,
                        error = null
                    )

                    // Optional: if selected spot disappears from DB, clear selection
                    val selectedId = _uiState.value.selectedParking?.id
                    if (selectedId != null && spaces.none { it.id == selectedId }) {
                        _uiState.value = _uiState.value.copy(selectedParking = null)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load parking spaces"
                )
            }
        }
    }

    fun selectParking(space: ParkingSpace?) {
        _uiState.value = _uiState.value.copy(selectedParking = space)
    }

    fun clearSelection() = selectParking(null)

    fun searchByLocationId(query: String) {
        val q = query.trim()

        if (q.isBlank()) {
            // reset search
            _uiState.value = _uiState.value.copy(
                parkingSpaces = allSpaces,   // ✅ show all markers again
                selectedParking = null,
                error = null
            )
            return
        }

        // Try exact match first (best for Location ID)
        val match = allSpaces.firstOrNull {
            it.locationId.equals(q, ignoreCase = true)
        } ?: allSpaces.firstOrNull {
            it.locationId.contains(q, ignoreCase = true)
        }

        if (match != null) {
            _uiState.value = _uiState.value.copy(
                parkingSpaces = allSpaces,      // ✅ OPTION A: keep all markers visible
                selectedParking = match,        // ✅ opens bottom sheet
                error = null
            )
            return // ✅ IMPORTANT: do not continue and overwrite state with filtering
        }

        // No match found
        _uiState.value = _uiState.value.copy(
            parkingSpaces = allSpaces,          // ✅ keep all markers visible
            selectedParking = null,
            error = "No parking found for ID: $q"
        )
    }
}
