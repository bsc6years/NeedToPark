package com.example.parkingfinder.ui.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.parkingfinder.ui.auth.PasswordStrengthMeter
import com.example.parkingfinder.util.calculatePasswordStrength
import com.example.parkingfinder.util.isValidPassword
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePasswordBottomSheet(
    onDismiss: () -> Unit,
    onPasswordUpdated: () -> Unit
) {
    var previousPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var previousPasswordError by remember { mutableStateOf(false) }
    var newPasswordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }

    var passwordErrorMessage by remember { mutableStateOf<String?>(null) }
    var generalErrorMessage by remember { mutableStateOf<String?>(null) }

    // This is for the password strength meter, same idea as sign up
    val passwordStrength = calculatePasswordStrength(newPassword)

    // This makes sure the strength meter appears when the user taps the new password field
    var passwordHasFocus by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }

    val isFormValid =
        previousPassword.isNotBlank() &&
                isValidPassword(newPassword) &&
                newPassword == confirmPassword

    // Keep the same field style feel as the sign up screen
    val fieldShape = RoundedCornerShape(10.dp)
    val fieldModifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
    val fieldGap = 14.dp

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
                text = "Update Password",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Enter your previous and new password",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = previousPassword,
                onValueChange = {
                    previousPassword = it
                    previousPasswordError = false
                    generalErrorMessage = null
                },
                placeholder = { Text("Previous Password") },
                isError = previousPasswordError,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                shape = fieldShape,
                modifier = fieldModifier
            )

            if (previousPasswordError) {
                Text(
                    text = "Please enter your previous password",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(fieldGap))

            OutlinedTextField(
                value = newPassword,
                onValueChange = {
                    newPassword = it
                    generalErrorMessage = null

                    passwordErrorMessage = when {
                        it.length < 8 ->
                            "Password must be at least 8 characters"

                        !it.any { char -> char.isLetter() } ->
                            "Password must contain a letter"

                        !it.any { char -> char.isDigit() } ->
                            "Password must contain a number"

                        !it.any { char -> !char.isLetterOrDigit() } ->
                            "Password must contain a special character"

                        else -> null
                    }

                    newPasswordError = passwordErrorMessage != null
                    confirmPasswordError = confirmPassword.isNotBlank() && confirmPassword != newPassword
                },
                placeholder = { Text("New Password") },
                isError = newPasswordError,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                shape = fieldShape,
                modifier = fieldModifier
                    .onFocusChanged { focusState ->
                        passwordHasFocus = focusState.isFocused
                    }
            )

            // Same behaviour as sign up - only show meter when password field is focused
            if (passwordHasFocus) {
                Spacer(modifier = Modifier.height(8.dp))
                PasswordStrengthMeter(strength = passwordStrength)
            }

            if (passwordErrorMessage != null) {
                Text(
                    text = passwordErrorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(fieldGap))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    generalErrorMessage = null
                    confirmPasswordError = it != newPassword
                },
                placeholder = { Text("Confirm Password") },
                isError = confirmPasswordError,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                shape = fieldShape,
                modifier = fieldModifier
            )

            if (confirmPasswordError) {
                Text(
                    text = "Password does not match!",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                )
            }

            if (generalErrorMessage != null) {
                Text(
                    text = generalErrorMessage!!,
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
                    previousPasswordError = previousPassword.isBlank()
                    newPasswordError = !isValidPassword(newPassword)
                    confirmPasswordError = confirmPassword != newPassword

                    if (!previousPasswordError && !newPasswordError && !confirmPasswordError) {
                        val user = FirebaseAuth.getInstance().currentUser
                        val email = user?.email

                        if (user == null || email == null) {
                            generalErrorMessage = "No logged-in user found."
                            return@Button
                        }

                        isLoading = true
                        generalErrorMessage = null

                        val credential = EmailAuthProvider.getCredential(email, previousPassword)

                        user.reauthenticate(credential)
                            .addOnSuccessListener {
                                user.updatePassword(newPassword)
                                    .addOnSuccessListener {
                                        isLoading = false
                                        onPasswordUpdated()
                                    }
                                    .addOnFailureListener { e ->
                                        isLoading = false
                                        generalErrorMessage =
                                            e.localizedMessage ?: "Failed to update password."
                                    }
                            }
                            .addOnFailureListener { e ->
                                isLoading = false
                                generalErrorMessage =
                                    e.localizedMessage ?: "Previous password is incorrect."
                            }
                    }
                },
                enabled = !isLoading && isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text("Update Password")
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