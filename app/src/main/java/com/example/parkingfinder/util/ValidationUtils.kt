package com.example.parkingfinder.util

import android.util.Patterns

/*As a core functionality of the app is data protection,
the function below makes sure the user enters a strong password the has a range of charecters to make it a stronger password.
This helps with achieving one of the core goals*/
fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isValidPassword(password: String): Boolean {
    val hasLetter = password.any { it.isLetter() }
    val hasDigit = password.any { it.isDigit() }
    val hasSpecialChar = password.any { !it.isLetterOrDigit() }
    val isLongEnough = password.length >= 8

    return hasLetter && hasDigit && hasSpecialChar && isLongEnough
}

enum class PasswordStrength {
    WEAK,
    MEDIUM,
    STRONG
}

fun calculatePasswordStrength(password: String): PasswordStrength {
    var score = 0

    if (password.length >= 8) score++
    if (password.any { it.isLetter() }) score++
    if (password.any { it.isDigit() }) score++
    if (password.any { !it.isLetterOrDigit() }) score++

    return when (score) {
        0, 1 -> PasswordStrength.WEAK
        2, 3 -> PasswordStrength.MEDIUM
        4 -> PasswordStrength.STRONG
        else -> PasswordStrength.WEAK
    }
}