package com.example.parkingfinder.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ForgotPasswordScreen(
    onBackToLogin: () -> Unit
) {
    // Holds the email input
    var email by remember { mutableStateOf("") }

    // Holds success or error message shown to user
    var message by remember { mutableStateOf<String?>(null) }

    // Prevents repeated taps while Firebase request is running
    var isLoading by remember { mutableStateOf(false) }

    // Very simple email check for empty input
    val isEmailValid = email.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 48.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        // App title
        Text(
            text = "Need to Park",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Parking made simpler",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(60.dp))

        // Screen heading
        Text(
            text = "Forgotten Password",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Enter your email to reset your password",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Email input
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                message = null // clear old message when user types again
            },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Send reset link button
        Button(
            onClick = {
                if (!isEmailValid) {
                    message = "Please enter your email address."
                    return@Button
                }

                isLoading = true
                message = null

                FirebaseAuth.getInstance()
                    .sendPasswordResetEmail(email.trim())
                    .addOnCompleteListener { task ->
                        isLoading = false

                        message = if (task.isSuccessful) {
                            "If this email is registered to a valid account, a password reset link has been sent. " +
                                    "Make sure to check your spam folder." +
                                    "If a link has not been sent within 5 minutes, then retry"
                        } else {
                            task.exception?.localizedMessage
                                ?: "Something went wrong. Please try again."
                        }
                    }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier.height(18.dp)
                )
            } else {
                Text("Send Reset Link")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Result message
        message?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onBackToLogin) {
            Text("Back to Login")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ForgotPasswordScreenPreview() {
    MaterialTheme {
        ForgotPasswordScreen(
            onBackToLogin = {}
        )
    }
}