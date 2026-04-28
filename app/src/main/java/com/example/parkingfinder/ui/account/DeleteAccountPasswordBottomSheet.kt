package com.example.parkingfinder.ui.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAccountPasswordBottomSheet(
    onDismiss: () -> Unit,
    onDeleteConfirmed: (String) -> Unit,
    errorMessage: String? = null,
    isLoading: Boolean = false
) {
    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFF5F0F7)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 12.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Confirm Account Deletion",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Enter your password to continue.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = false
                },
                placeholder = { Text("Password") },
                singleLine = true,
                isError = passwordError,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )

            if (passwordError) {
                Text(
                    text = "Please enter your password",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                )
            }

            if (!errorMessage.isNullOrBlank()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (password.isBlank()) {
                        passwordError = true
                    } else {
                        onDeleteConfirmed(password)
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text("Delete Account")
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