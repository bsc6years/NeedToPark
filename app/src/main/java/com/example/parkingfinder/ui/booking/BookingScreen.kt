package com.example.parkingfinder.ui.booking

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.parkingfinder.model.ParkingSpace
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
import com.example.parkingfinder.model.Vehicle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import androidx.compose.foundation.layout.FlowRow

import androidx.compose.ui.text.style.TextAlign

private enum class BookingStep { VEHICLE, DURATION, CONFIRM, ACTIVE }

@Composable
fun BookingScreen(
    space: ParkingSpace,
    onClose: () -> Unit,
    viewModel: BookingViewModel = hiltViewModel()
) {
    var step by remember { mutableStateOf(BookingStep.VEHICLE) }

/*
    //The following two lines were used for the MVP product for PSDP
    var vehicleReg by remember { mutableStateOf("") }
    var durationMinutes by remember { mutableIntStateOf(60) }
    */

    var selectedVehicle by remember { mutableStateOf<Vehicle?>(null) }
    var vehicles by remember { mutableStateOf<List<Vehicle>>(emptyList()) }
    var durationMinutes by remember { mutableIntStateOf(60) } // time is set to 60 mins

    val currentUser = FirebaseAuth.getInstance().currentUser
    val firestore = FirebaseFirestore.getInstance()

/*    // For the confirmation screen we’ll show a simple "from-to" time window
    val startMillis = remember { System.currentTimeMillis() }
    val endMillis = remember(durationMinutes) { startMillis + durationMinutes * 60_000L }
    */

    var previewNowMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(step) {
        while (step == BookingStep.CONFIRM) {
            previewNowMillis = System.currentTimeMillis()
            kotlinx.coroutines.delay(1000L)
        }
    }

    val previewStartMillis = previewNowMillis
    val previewEndMillis = previewStartMillis + durationMinutes * 60_000L

    val uiState by viewModel.uiState.collectAsState()

    val totalPrice = remember(space.pricePerHour, durationMinutes) {
        space.pricePerHour * (durationMinutes / 60.0)
    }

    //Loads saved vehicles
    LaunchedEffect(currentUser?.uid) {
        val uid = currentUser?.uid ?: return@LaunchedEffect

        firestore.collection("vehicles")
            .whereEqualTo("userId", uid)
            .get()
            .addOnSuccessListener { result ->
                vehicles = result.documents.mapNotNull { doc ->
                    doc.toObject(Vehicle::class.java)?.copy(id = doc.id)
                }

                if (selectedVehicle == null && vehicles.isNotEmpty()) {
                    selectedVehicle = vehicles.first()
                }
            }
            .addOnFailureListener {
                vehicles = emptyList()
            }
    }

    // If booking saved successfully, jump to ACTIVE screen
    LaunchedEffect(uiState.successBookingId) {
        if (uiState.successBookingId != null) step = BookingStep.ACTIVE
    }

    when (step) {
        BookingStep.VEHICLE -> VehicleStep(
            spaceName = space.name,
            locationId = space.locationId,
            vehicles = vehicles,
            selectedVehicle = selectedVehicle,
            onVehicleSelected = { selectedVehicle = it },
            onCancel = {
                viewModel.reset()
                onClose()
            },
            onConfirm = {
                step = BookingStep.DURATION
            }
        )

        BookingStep.DURATION -> DurationStep(
            spaceName = space.name,
            locationId = space.locationId,
            selectedVehicle = selectedVehicle,
            durationMinutes = durationMinutes,
            onDurationChange = { durationMinutes = it },
            onBack = { step = BookingStep.VEHICLE },
            onCancel = {
                viewModel.reset()
                onClose()
            },
            onConfirm = { step = BookingStep.CONFIRM }
        )

        BookingStep.CONFIRM -> ConfirmStep(
            spaceName = space.name,
            locationId = space.locationId,
            vehicleReg = selectedVehicle?.registration.orEmpty(),
            durationMinutes = durationMinutes,
            startMillis = previewStartMillis,
            endMillis = previewEndMillis,
            totalPrice = totalPrice,
            error = uiState.error,
            isSubmitting = uiState.isSubmitting,
            onBack = { step = BookingStep.DURATION },
            onCancel = {
                viewModel.reset()
                onClose()
            },
            onBook = {
                viewModel.submitBooking(
                    space = space,
                    vehicleId = selectedVehicle?.id.orEmpty(),
                    vehicleReg = selectedVehicle?.registration.orEmpty(),
                    durationMinutes = durationMinutes
                )
            }
        )

        BookingStep.ACTIVE -> ActiveSessionStep(
            spaceName = space.name,
            locationId = space.locationId,
            totalPrice = totalPrice,
            endMillis = previewEndMillis,
            // The following code was used for the PSDP and to get the app started and running
            // It let me book a new parking session for testing purposes.
/*            onBookNew = {
                viewModel.reset()
                step = BookingStep.VEHICLE
                selectedVehicle = vehicles.firstOrNull()
                durationMinutes = 60
            },
*/
            onDone = {
                viewModel.reset()
                onClose()
            }
        )
    }
}

@Composable
private fun VehicleStep(
    spaceName: String,
    locationId: String,
    vehicles: List<Vehicle>,
    selectedVehicle: Vehicle?,
    onVehicleSelected: (Vehicle) -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Which vehicle do you want to park?",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(12.dp))

        Text(spaceName)
        Text(
            "Location ID: $locationId",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(16.dp))

        if (vehicles.isEmpty()) {
            Text(
                "No saved vehicles found. Add a vehicle from your account page.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            vehicles.forEach { vehicle ->
                val isSelected = selectedVehicle?.id == vehicle.id

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onVehicleSelected(vehicle) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFFD8CCFF) else Color(0xFFEDE3F8)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = vehicle.registration,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = vehicle.make,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = vehicle.colour,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onConfirm,
            enabled = selectedVehicle != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirm vehicle")
        }

        Spacer(Modifier.height(10.dp))

        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
    }
}

@Composable
private fun DurationStep(
    spaceName: String,
    locationId: String,
    selectedVehicle: Vehicle?,
    durationMinutes: Int,
    onDurationChange: (Int) -> Unit,
    onBack: () -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit

) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "How long do you want to park for?",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(12.dp))

        Text(spaceName)
        Text(
            "Location ID: $locationId",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Vehicle Reg: ${selectedVehicle?.registration.orEmpty()}",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(16.dp))

        Text("Duration", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(5, 10, 15, 30, 45, 60, 90, 120).forEach { mins ->
                FilterChip(
                    selected = durationMinutes == mins,
                    onClick = { onDurationChange(mins) },
                    label = { Text("${mins}m") }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            "Be aware, some locations may be time limited. You must check this against the location timeplate to avoid receiving a Penalty Charge Notice.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirm")
        }

        Spacer(Modifier.height(10.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }

        Spacer(Modifier.height(10.dp))

        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
    }
}

@Composable
private fun ConfirmStep(
    spaceName: String,
    locationId: String,
    vehicleReg: String,
    durationMinutes: Int,
    startMillis: Long,
    endMillis: Long,
    totalPrice: Double,
    error: String?,
    isSubmitting: Boolean,
    onBack: () -> Unit,
    onCancel: () -> Unit,
    onBook: () -> Unit
) {
    val timeFmt = remember { SimpleDateFormat("H:mm", Locale.getDefault()) }
    val from = remember(startMillis) { timeFmt.format(Date(startMillis)) }
    val to = remember(endMillis) { timeFmt.format(Date(endMillis)) }
    val durationLabel = if (durationMinutes % 60 == 0) {
        "${durationMinutes / 60} HOUR"
    } else {
        "${durationMinutes} MINUTES"
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Check details of parking session and confirm", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        Text("Location:", style = MaterialTheme.typography.titleMedium)
        Text(spaceName)
        Text("Location ID: $locationId", color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(Modifier.height(12.dp))

        Text("Vehicle:", style = MaterialTheme.typography.titleMedium)
        Text(vehicleReg.ifBlank { "—" })

        Spacer(Modifier.height(12.dp))

        Text("Duration:", style = MaterialTheme.typography.titleMedium)
        Text(durationLabel)

        Spacer(Modifier.height(12.dp))

        Text("From: $from")
        Text("To: $to")

        Spacer(Modifier.height(12.dp))

        Text("Cost: £${"%.2f".format(totalPrice)}", style = MaterialTheme.typography.titleMedium)

        error?.let {
            Spacer(Modifier.height(12.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onBook,
            enabled = !isSubmitting,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isSubmitting) "Booking..." else "Book Parking Session / Pay")
        }

        Spacer(Modifier.height(10.dp))

        OutlinedButton(
            onClick = onBack,
            enabled = !isSubmitting,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }

        Spacer(Modifier.height(10.dp))

        OutlinedButton(
            onClick = onCancel,
            enabled = !isSubmitting,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
    }
}

@Composable
private fun ActiveSessionStep(
    spaceName: String,
    locationId: String,
    totalPrice: Double,
    endMillis: Long,
    onDone: () -> Unit
) {
    // Update "now" every second to create a countdown
    var nowMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(endMillis) {
        while (true) {
            nowMillis = System.currentTimeMillis()
            kotlinx.coroutines.delay(1000L)
        }
    }

    val remainingMillis = (endMillis - nowMillis).coerceAtLeast(0L)

    // Format remaining time as mm:ss or h:mm:ss
    val remainingText = remember(remainingMillis) {
        val totalSeconds = remainingMillis / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        if (hours > 0) {
            String.format("%d:%02d:%02d Remaining", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d Remaining", minutes, seconds)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Countdown timer
                Text(
                    text = remainingText,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = spaceName,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Location ID: $locationId",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Cost: £${"%.2f".format(totalPrice)}",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(Modifier.height(16.dp))
/*
//Old button for PSDP Demo
        Button(
            onClick = onBookNew,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Book A New Parking Session")
        }

*/
        Spacer(Modifier.height(10.dp))

        OutlinedButton(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Done")
        }
    }
}

