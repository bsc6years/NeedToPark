package com.example.parkingfinder.ui.vehicle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.parkingfinder.model.Vehicle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AddVehicleScreen(
    registration: String,
    onRegistrationChange: (String) -> Unit,
    make: String,
    onMakeChange: (String) -> Unit,
    model: String,
    onModelChange: (String) -> Unit,
    colour: String,
    onColourChange: (String) -> Unit,
    isFormValid: Boolean,
    onConfirmClick: () -> Unit
) {
    // UI-only screen.
    // This draws the fields and button, but does not talk to Firestore directly.
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
            text = "Enter the new vehicle's registration plate",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = registration,
            onValueChange = onRegistrationChange,
            label = { Text("Vehicle Registration") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = make,
            onValueChange = onMakeChange,
            label = { Text("Make") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = model,
            onValueChange = onModelChange,
            label = { Text("Model") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = colour,
            onValueChange = onColourChange,
            label = { Text("Colour") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Make sure the details are correct or else you may be liable for a Penalty Charge Notice",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onConfirmClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = isFormValid,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFormValid) Color(0xFF6A4FB3) else Color(0xFFD9CBEF)
            )
        ) {
            Text("Add Vehicle")
        }
    }
}

@Composable
fun AddVehicleRoute(
    onVehicleSaved: () -> Unit
) {
    // Route/container composable.
    // This holds the input state and saves the vehicle to Firestore.

    val currentUser = FirebaseAuth.getInstance().currentUser
    val firestore = FirebaseFirestore.getInstance()

    // Local state for the form fields.
    var registration by remember { mutableStateOf("") }
    var make by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var colour by remember { mutableStateOf("") }

    val isFormValid =
        registration.isNotBlank() &&
                make.isNotBlank() &&
                model.isNotBlank()&&
                colour.isNotBlank()

    AddVehicleScreen(
        registration = registration,
        onRegistrationChange = { registration = it.uppercase() },
        make = make,
        onMakeChange = { make = it },
        model = model,
        onModelChange = { model = it },
        colour = colour,
        onColourChange = { colour = it },
        isFormValid = isFormValid,
        onConfirmClick = {
            val uid = currentUser?.uid ?: return@AddVehicleScreen

            // Build the Vehicle object that will be saved to Firestore.
            val vehicle = Vehicle(
                userId = uid,
                registration = registration.trim(),
                make = make.trim(),
                model = model.trim(),
                colour = colour.trim(),
                createdAtMillis = System.currentTimeMillis()
            )

            // Save the vehicle into the vehicles collection.
            firestore.collection("vehicles")
                .add(vehicle)
                .addOnSuccessListener {
                    // After saving successfully, go back to the Vehicles page.
                    onVehicleSaved()
                }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AddVehicleScreenPreview() {
    AddVehicleScreen(
        registration = "KE33 CLR",
        onRegistrationChange = {},
        make = "Tesla",
        onMakeChange = {},
        model = "Model 3",
        onModelChange = {},
        colour = "Red",
        onColourChange = {},
        isFormValid = true,
        onConfirmClick = {}
    )
}