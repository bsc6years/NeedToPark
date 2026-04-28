package com.example.parkingfinder.ui.auth

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.parkingfinder.util.PasswordStrength
import com.example.parkingfinder.util.calculatePasswordStrength
import com.example.parkingfinder.util.isValidEmail
import com.example.parkingfinder.util.isValidPassword

import androidx.compose.material3.ButtonDefaults
import com.example.parkingfinder.ui.theme.BrandBlue


fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}


@Composable
fun PasswordStrengthMeter(strength: PasswordStrength) {
    val (text, progress) = when (strength) {
        PasswordStrength.WEAK -> "Weak" to 0.33f
        PasswordStrength.MEDIUM -> "Medium" to 0.66f
        PasswordStrength.STRONG -> "Strong" to 1f
    }

    val color = when (strength) {
        PasswordStrength.WEAK -> MaterialTheme.colorScheme.error
        PasswordStrength.MEDIUM -> MaterialTheme.colorScheme.tertiary
        PasswordStrength.STRONG -> MaterialTheme.colorScheme.primary
    }

    Column {
        LinearProgressIndicator(
            progress = progress,
            color = color,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Password strength: $text",
            color = color,
            style = MaterialTheme.typography.bodySmall
        )
    }
}


@Composable
fun SignUpScreen(
    onGoToLogin: () -> Unit,
    onSignUpSuccess: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    // State - it states what each var is and needs to remember it
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    /*this is for the password strength meter, it makes use of the coding in validationutils.kt*/
    val passwordStrength = calculatePasswordStrength(password)

    // This is for picking up errors and to help the app user if they make a mistake with registration. UI feature
    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }

    var passwordErrorMessage by remember { mutableStateOf<String?>(null) }

    /*    The following line of code helps with the UX
    It makes sure the strength meter will show up when the user clicks on the password field*/
    var passwordHasFocus by remember { mutableStateOf(false) }

    val isFormValid =
        name.isNotBlank() &&
                email.isNotBlank() &&
                isValidEmail(email) &&
                isValidPassword(password) &&
                password == confirmPassword

    // Figma-like sizing & spacing
    val fieldShape = RoundedCornerShape(10.dp)
    val fieldModifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
    val fieldGap = 14.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Top left link (Figma: "Sign In Instead")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            TextButton(
                onClick = onGoToLogin,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Sign In Instead")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Big title + subtitle (Figma)
        Text(
            text = "Need to Park",
            style = MaterialTheme.typography.headlineLarge,
            color = BrandBlue
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Parking made simpler",
            style = MaterialTheme.typography.bodySmall,
            color = BrandBlue.copy(alpha = 0.65f)
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Create an account",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Enter your details to sign up for this app",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                nameError = false
            },
            // Figma uses placeholders instead of labels
            placeholder = { Text("Name") },
            isError = nameError,
            singleLine = true,
            shape = fieldShape,
            modifier = fieldModifier
        )

        Spacer(modifier = Modifier.height(fieldGap))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = !isValidEmail(it)
            },
            placeholder = { Text("email@domain.com") },
            isError = emailError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            singleLine = true,
            shape = fieldShape,
            modifier = fieldModifier
        )

        if (emailError) {
            Text(
                text = "Enter a valid email address",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(fieldGap))

        /*        OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = false
                        passwordError = it.length < 8
                    },
                    label = { Text("Password") },
                    isError = passwordError,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                if (passwordError) {
                    Text(
                        text = "Password must have 8 characters",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }*/

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it

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

                passwordError = passwordErrorMessage != null
            },
            // Figma uses placeholders
            placeholder = { Text("Password") },
            isError = passwordError,
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            shape = fieldShape,
            modifier = fieldModifier
                .onFocusChanged { focusState ->
                    passwordHasFocus = focusState.isFocused
                }
        )

        /*        next two lines are for the pass word stregth meter and when password is clicked*/
        if (passwordHasFocus) {
            Spacer(modifier = Modifier.height(8.dp))
            PasswordStrengthMeter(strength = passwordStrength)
        }

        /*can use the following if the meter should stay displayed or not*/
        /*To keep the UI looking simple, it was decided the following code not to be used*/
        /*        if (passwordHasFocus || password.isNotEmpty()) {
                    PasswordStrengthMeter(strength = passwordStrength)
                }*/

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
                confirmPasswordError = false
                confirmPasswordError = it != password
            },
            placeholder = { Text("Confirm Password") },
            isError = confirmPasswordError,
            visualTransformation = PasswordVisualTransformation(),
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

        Spacer(modifier = Modifier.height(22.dp))

        /*        Button(
                    onClick = { *//* sign up logic *//* },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }*/

        // Figma-style primary button
        Button(
            onClick = {
                nameError = name.isBlank()
                emailError = !isValidEmail(email)
                passwordError = password.length < 8
                confirmPasswordError = confirmPassword != password

                if (!nameError &&
                    !emailError &&
                    !passwordError &&
                    !confirmPasswordError
                ) {
                    //This is the process that will have in firebase datastore
                    authViewModel.signUp(
                        name = name,
                        email = email,
                        password = password
                    ) { success, errorMessage ->
                        if (success) {
                            onSignUpSuccess()
                        } else {
                            passwordErrorMessage = errorMessage
                        }
                    }
                }
            },
            enabled = isFormValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Agree & Continue")
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Terms text (Figma-like)
        Text(
            text = "By clicking continue, you agree to our Terms of Service.\n" +
                    "We will only process your data in accordance to and as\n" +
                    "outlined in our Privacy Policy.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 6.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Keep your existing link too (useful for report/testing)
        TextButton(onClick = onGoToLogin) {
            Text("Already have an account? Log in")
        }
    }
}


/*@Composable
fun SignUpScreen(
    onGoToLogin: () -> Unit,
    onSignUpSuccess: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
    )
{
    // State - it states what each var is and needs to rembember it
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    *//*this is for the password strength meter, it makes use of the coding in validationutils.kt*//*
    val passwordStrength = calculatePasswordStrength(password)

    // This is for picking up errors and to help the app user if they make a mistake with registration. UI feature
    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }

    var passwordErrorMessage by remember { mutableStateOf<String?>(null) }

    *//*    The following line of code helps with the UX
    It makes sure the strength meter will show up when the user clicks on the password field*//*
    var passwordHasFocus by remember { mutableStateOf(false) }

    val isFormValid =
        name.isNotBlank() &&
                email.isNotBlank() &&
                isValidEmail(email) &&
                isValidPassword(password) &&
                password == confirmPassword

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                nameError = false
            },
            label = { Text("Full Name") },
            isError = nameError,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = !isValidEmail(it)
            },
            label = { Text("Email") },
            isError = emailError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (emailError) {
            Text(
                text = "Enter a valid email address",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

*//*        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = false
                passwordError = it.length < 8
            },
            label = { Text("Password") },
            isError = passwordError,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (passwordError) {
            Text(
                text = "Password must have 8 characters",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }*//*

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it

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

                passwordError = passwordErrorMessage != null
            },
            label = { Text("Password") },
            placeholder = { Text("Enter a Strong Password") },
            isError = passwordError,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    passwordHasFocus = focusState.isFocused
                }
        )

*//*        next two lines are for the pass word stregth meter and when password is clicked*//*
        if (passwordHasFocus) {
            Spacer(modifier = Modifier.height(8.dp))
            PasswordStrengthMeter(strength = passwordStrength)
        }

        *//*can use the following if the meter should stay displayed or not*//*
        *//*To keep the UI looking simple, it was decided the following code not to be used*//*
*//*        if (passwordHasFocus || password.isNotEmpty()) {
            PasswordStrengthMeter(strength = passwordStrength)
        }*//*

        if (passwordErrorMessage != null) {
            Text(
                text = passwordErrorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                confirmPasswordError = false
                confirmPasswordError = it != password
            },
            label = { Text("Confirm Password") },
   *//*         placeholder = { Text("Confirm Password") },*//*
            isError = confirmPasswordError,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
        )

        if (confirmPasswordError) {
            Text(
                text = "Password does not match!",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        TextButton(onClick = onGoToLogin) {
            Text("Already have an account? Log in")
        }

*//*        Button(
            onClick = { *//**//* sign up logic *//**//* },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }*//*


        Button(
            onClick = {
                nameError = name.isBlank()
                emailError = !isValidEmail(email)
                passwordError = password.length < 8
                confirmPasswordError = confirmPassword != password

                if (!nameError &&
                    !emailError &&
                    !passwordError &&
                    !confirmPasswordError
                ) {
                    //This is the process that will have in firebase datastore
                    authViewModel.signUp(
                        name = name,
                        email = email,
                        password = password
                    ) { success, errorMessage ->
                        if (success) {
                            onSignUpSuccess()
                        } else {
                            passwordErrorMessage = errorMessage
                        }
                    }
                }
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }
    }
}*/



