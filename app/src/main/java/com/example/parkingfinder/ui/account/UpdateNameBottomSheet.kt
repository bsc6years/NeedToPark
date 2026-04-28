package com.example.parkingfinder.ui.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateNameBottomSheet(
    currentName: String,
    onDismiss: () -> Unit,
    onNameUpdated: (String) -> Unit
) {
    var newName by remember { mutableStateOf(currentName) }
    var nameError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 12.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Update Name",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Enter Name",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = newName,
                onValueChange = {
                    newName = it
                    nameError = false
                    errorMessage = null
                },
                placeholder = { Text("Name") },
                singleLine = true,
                isError = nameError,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            )

            if (nameError) {
                Text(
                    text = "Please enter a valid name",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                )
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = {
                    val trimmedName = newName.trim()
                    nameError = trimmedName.isBlank()

                    if (!nameError) {
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        val uid = currentUser?.uid

                        if (uid == null) {
                            errorMessage = "No logged-in user found."
                            return@Button
                        }

                        isLoading = true
                        errorMessage = null

                        FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(uid)
                            .update("name", trimmedName)
                            .addOnSuccessListener {
                                isLoading = false
                                onNameUpdated(trimmedName)
                            }
                            .addOnFailureListener { e ->
                                isLoading = false
                                errorMessage = e.localizedMessage ?: "Failed to update name."
                            }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text("Update Name")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(
                onClick = onDismiss,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Cancel")
            }
        }
    }
}