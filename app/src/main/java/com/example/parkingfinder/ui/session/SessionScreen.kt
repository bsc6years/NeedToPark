package com.example.parkingfinder.ui.session

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.parkingfinder.model.Booking
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import androidx.compose.ui.text.style.TextAlign

data class ActiveSessionUi(
    //    val remainingTime: String, it requreid to compose the screen to update timer it didnt countdown live
    val endTimeMillis: Long,
    val locationId: String,
    val spaceName: String,
    val totalPrice: Double,
    val vehicleReg: String
)

@Composable
fun SessionScreen(
    activeSession: ActiveSessionUi?,
    onBookNewSessionClick: () -> Unit
) {
    var currentTimeMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(activeSession?.endTimeMillis) {
        while (activeSession != null && currentTimeMillis < activeSession.endTimeMillis) {
            currentTimeMillis = System.currentTimeMillis()
            delay(1000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (activeSession != null) {
            val remainingTime = formatRemainingTime(activeSession.endTimeMillis - currentTimeMillis)

            ActiveSessionCard(
                remainingTime = remainingTime,
                locationId = activeSession.locationId,
                spaceName = activeSession.spaceName,
                totalPrice = activeSession.totalPrice,
                vehicleReg = activeSession.vehicleReg,
                onExtendClick = {
                    // Placeholder for future extend session function
                }
            )
        } else {
            Text(
                text = "No Active Parking Session",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onBookNewSessionClick,
            modifier = Modifier.fillMaxWidth(0.8f),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6A4FB3)
            )
        ) {
            Text("Book a New Parking Session")
        }
    }
}

@Composable
fun ActiveSessionCard(
    remainingTime: String,
    locationId: String,
    spaceName: String,
    totalPrice: Double,
    vehicleReg: String,
    onExtendClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.Red),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.AccessTime,
                contentDescription = "Session time"
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = remainingTime,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = spaceName,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            //will show the price to two decimal points:
            val formattedPrice = "£%.2f".format(totalPrice)

            Text(
                text = "$locationId --- $vehicleReg --- $formattedPrice",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onExtendClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6A4FB3)
                )
            ) {
                Text("Extend")
            }
        }
    }
}

@Composable
fun SessionRoute(
    onBookNewSessionClick: () -> Unit
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val firestore = FirebaseFirestore.getInstance()

    var activeSession by remember { mutableStateOf<ActiveSessionUi?>(null) }

    LaunchedEffect(currentUser?.uid) {
        val uid = currentUser?.uid ?: return@LaunchedEffect

        firestore.collection("bookings")
            .whereEqualTo("userId", uid)
            .get()
            .addOnSuccessListener { result ->
                val now = System.currentTimeMillis()

                val activeBooking = result.documents
                    .mapNotNull { doc -> doc.toObject(Booking::class.java)?.copy(id = doc.id) }
                    .firstOrNull { booking ->
                        booking.status == "active" &&
                                now in booking.startTimeMillis..booking.endTimeMillis
                    }

                activeSession = activeBooking?.let { booking ->
                    ActiveSessionUi(
                        /*                        remainingTime = formatRemainingTime(booking.endTimeMillis - now),*/
                        endTimeMillis = booking.endTimeMillis,
                        locationId = booking.locationId,
                        spaceName = booking.spaceName,
                        totalPrice = booking.totalPrice,
                        vehicleReg = booking.vehicleReg
                    )
                }
            }
            .addOnFailureListener {
                activeSession = null
            }
    }

    SessionScreen(
        activeSession = activeSession,
        onBookNewSessionClick = onBookNewSessionClick
    )
}

//to display the countdown for the parking session remaining time
fun formatRemainingTime(millisRemaining: Long): String {
    if (millisRemaining <= 0) return "Expired"

    val totalSeconds = millisRemaining / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return if (hours > 0) {
        String.format("%d:%02d:%02d Remaining", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d Remaining", minutes, seconds)
    }
}

//shows the preview of the UI for parking session screen with some hardcoded data
@Preview(showBackground = true)
@Composable
fun SessionScreenPreview() {
    SessionScreen(
        activeSession = ActiveSessionUi(
            endTimeMillis = System.currentTimeMillis() + 3_888_000,
            locationId = "79596",
            spaceName = "West Smithfield Street",
            totalPrice = 15.0,
            vehicleReg = "AA08 AAA"
        ),
        onBookNewSessionClick = {}
    )
}