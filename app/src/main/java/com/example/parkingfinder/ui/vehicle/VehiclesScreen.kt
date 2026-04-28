package com.example.parkingfinder.ui.vehicle

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.parkingfinder.model.Vehicle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun VehiclesScreen(
    vehicles: List<Vehicle>,
    onAddVehicleClick: () -> Unit,
    onVehicleClick: (String) -> Unit
) {
    // UI-only screen.
    // It simply displays the vehicles passed into it.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Need to Park",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color(0xFF3D33FF),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Your vehicles",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(20.dp))

        // If no vehicles exist yet, show a simple helper message.
        if (vehicles.isEmpty()) {
            Text(
                text = "No vehicles added yet.",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))
        } else {
            // Show each saved vehicle as a card.
            vehicles.forEach { vehicle ->
                VehicleItemCard(
                    vehicle = vehicle,
                    onClick = {
                        onVehicleClick(vehicle.id)
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // Add vehicle card/button
        AddVehicleCard(
            onClick = onAddVehicleClick
        )
    }
}

@Composable
fun VehicleItemCard(
    vehicle: Vehicle,
    onClick: () -> Unit
) {
    // Reusable UI card for one saved vehicle.
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEDE3F8)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.DirectionsCar,
                contentDescription = "Vehicle",
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.size(12.dp))

            Column {
                Text(
                    text = vehicle.registration,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "${vehicle.make} ${vehicle.model} (${vehicle.colour})",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun AddVehicleCard(
    onClick: () -> Unit
) {
    // Simple clickable card that opens the Add Vehicle page.
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEDE3F8)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.AddCircle,
                contentDescription = "Add vehicle",
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.size(12.dp))

            Text(
                text = "Add vehicle",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun VehiclesRoute(
    onAddVehicleClick: () -> Unit,
    onVehicleClick: (String) -> Unit
) {
    // Route/container composable.
    // This handles Firebase Auth + Firestore loading.
    // The UI screen above only displays the data.

    val currentUser = FirebaseAuth.getInstance().currentUser
    val firestore = FirebaseFirestore.getInstance()

    // Holds the loaded vehicles from Firestore.
    var vehicles by remember { mutableStateOf<List<Vehicle>>(emptyList()) }

    // Runs when the logged-in user changes.
    LaunchedEffect(currentUser?.uid) {
        val uid = currentUser?.uid ?: return@LaunchedEffect

        firestore.collection("vehicles")
            .whereEqualTo("userId", uid)
            .get()
            .addOnSuccessListener { result ->
                // Convert Firestore documents into Vehicle objects.
                vehicles = result.documents.mapNotNull { doc ->
                    doc.toObject(Vehicle::class.java)?.copy(id = doc.id)
                }
            }
            .addOnFailureListener {
                // If Firestore fails, keep the list empty for now.
                vehicles = emptyList()
            }
    }

    // Pass the loaded vehicles into the UI-only screen.
    VehiclesScreen(
        vehicles = vehicles,
        onAddVehicleClick = onAddVehicleClick,
        onVehicleClick = onVehicleClick
    )
}

@Preview(showBackground = true)
@Composable
fun VehiclesScreenPreview() {
    VehiclesScreen(
        vehicles = listOf(
            Vehicle(
                registration = "XX61 XXX",
                make = "Ford",
                colour = "Black"
            ),
            Vehicle(
                registration = "TE57 REG",
                make = "Mercedes Benz",
                colour = "Blue"
            )
        ),
        onAddVehicleClick = { },
        onVehicleClick = { }
    )
}