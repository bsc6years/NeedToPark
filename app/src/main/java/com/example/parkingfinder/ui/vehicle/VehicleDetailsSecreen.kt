package com.example.parkingfinder.ui.vehicle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.parkingfinder.model.Vehicle
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun VehicleDetailsScreen(
    vehicle: Vehicle?,
    onRemoveClick: () -> Unit,
    onDismissDeleteDialog: () -> Unit,
    showDeleteDialog: Boolean,
    onConfirmDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Need to Park",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color(0xFF3D33FF),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Vehicle details",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (vehicle == null) {
            Text(
                text = "Vehicle not found.",
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFEDE3F8)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Vehicle Registration: ${vehicle.registration}",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Vehicle Make: ${vehicle.make}",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Vehicle Model: ${vehicle.model}",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Vehicle Colour: ${vehicle.colour}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = onRemoveClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    //The colour for the remove vehicle:
                    containerColor = Color(0xFFB3261E)
                )
            ) {
                Text("Remove Vehicle")
            }
        }
    }

    if (showDeleteDialog && vehicle != null) {
        AlertDialog(
            onDismissRequest = onDismissDeleteDialog,
            title = {
                Text("Delete Vehicle")
            },
            text = {
                Text("You are about to delete vehicle ${vehicle.registration}. Are you sure you want to delete this vehicle?")
            },
            confirmButton = {
                TextButton(onClick = onConfirmDelete) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissDeleteDialog) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun VehicleDetailsRoute(
    vehicleId: String,
    onBackClick: () -> Unit,
    onVehicleDeleted: () -> Unit
) {
    val firestore = FirebaseFirestore.getInstance()

    var vehicle by remember { mutableStateOf<Vehicle?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(vehicleId) {
        if (vehicleId.isBlank()) return@LaunchedEffect

        firestore.collection("vehicles")
            .document(vehicleId)
            .get()
            .addOnSuccessListener { document ->
                vehicle = document.toObject(Vehicle::class.java)?.copy(id = document.id)
            }
            .addOnFailureListener {
                vehicle = null
            }
    }

    VehicleDetailsScreen(
        vehicle = vehicle,
        showDeleteDialog = showDeleteDialog,
        onRemoveClick = {
            showDeleteDialog = true
        },
        onDismissDeleteDialog = {
            showDeleteDialog = false
        },
        onConfirmDelete = {
            firestore.collection("vehicles")
                .document(vehicleId)
                .delete()
                .addOnSuccessListener {
                    showDeleteDialog = false
                    onVehicleDeleted()
                }
        }
    )
}

