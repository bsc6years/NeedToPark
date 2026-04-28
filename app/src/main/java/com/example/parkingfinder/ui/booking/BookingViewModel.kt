package com.example.parkingfinder.ui.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingfinder.data.BookingRepository
import com.example.parkingfinder.model.Booking
import com.example.parkingfinder.model.ParkingSpace
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BookingUiState(
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val successBookingId: String? = null
)

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingRepo: BookingRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    fun submitBooking(
        space: ParkingSpace,
        vehicleId: String,
        vehicleReg: String,
        durationMinutes: Int
    ) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            _uiState.value = BookingUiState(error = "You must be logged in to book.")
            return
        }

        if (vehicleId.isBlank()) {
            _uiState.value = BookingUiState(error = "Select a saved vehicle.")
            return
        }

        val reg = vehicleReg.trim()
        if (reg.isBlank()) {
            _uiState.value = BookingUiState(error = "Enter vehicle registration.")
            return
        }

        val startMillis = System.currentTimeMillis()
        val endMillis = startMillis + durationMinutes * 60_000L
        val totalPrice = space.pricePerHour * (durationMinutes / 60.0)

        val booking = Booking(
            userId = userId,
            spaceId = space.id,
            locationId = space.locationId,
            spaceName = space.name,
            vehicleId = vehicleId,
            vehicleReg = reg.uppercase(),
            durationMinutes = durationMinutes,
            startTimeMillis = startMillis,
            endTimeMillis = endMillis,
            pricePerHour = space.pricePerHour,
            totalPrice = totalPrice,

            // Payment-ready fields, for when a payment system can be introduced into the app
            paymentStatus = "unpaid",
            amount = totalPrice,
            currency = "GBP",

            // For now this can be kept active so the UI stays and behaves the same
            // Later when the payments exist, the status can change from pending to paid
            status = "active",

            createdAtMillis = System.currentTimeMillis()
        )

        viewModelScope.launch {
            _uiState.value = BookingUiState(isSubmitting = true)

            try {
                val bookingId = bookingRepo.createBooking(booking)
                _uiState.value = BookingUiState(successBookingId = bookingId)
            } catch (e: Exception) {
                _uiState.value = BookingUiState(
                    error = e.message ?: "Booking failed. Please try again."
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun reset() {
        _uiState.value = BookingUiState()
    }
}