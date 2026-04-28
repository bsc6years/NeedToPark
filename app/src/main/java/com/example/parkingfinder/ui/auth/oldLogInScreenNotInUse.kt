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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
/*import com.example.parkingfinder.ui.theme.BrandDefault*/


/*@Composable
fun LoginScreen(
    onGoToSignUp: () -> Unit,
    onLoginSuccess: () -> Unit
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = false
            },
            label = { Text("Email") },
            isError = emailError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = false
            },
            label = { Text("Password") },
            isError = passwordError,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                emailError = email.isBlank()
                passwordError = password.length < 8

                if (!emailError && !passwordError) {
                // login logic will go here later
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onGoToSignUp) {
            Text("Don't have an account? Sign up")
        }
    }
}*/

@Composable
fun oldLoginScreenNotInUse(
    onGoToSignUp: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Let Me Park",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "You Parking Companion App ",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = false
            },
            label = { Text("Email") },
            isError = emailError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = false
            },
            label = { Text("Password") },
            isError = passwordError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            /*            colors = ButtonDefaults.buttonColors(
                            containerColor = BrandDefault
                        ),*/
            onClick = {
                // For now, just call onLoginSuccess directly
                onLoginSuccess()
            }
        ) {
            Text("Login")
        }
        /*        Button(
                    onClick = {
                        emailError = email.isBlank()
                        passwordError = password.length < 8

                        if (!emailError && !passwordError) {
                            // TEMP: Navigate to map
                            // Later: replace with FirebaseAuth signIn
                            onLoginSuccess()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }*/

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onGoToSignUp) {
            Text("Don't have an account? Sign up")
        }
    }
}