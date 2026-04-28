package com.example.parkingfinder.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingfinder.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException


data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

private const val INVALID_LOGIN_MESSAGE =
    "Email and/or password is incorrect, or an account does not exist. " +
            "If you have forgotten your password then click the 'Reset Password' link."
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun setEmail(v: String) = update { it.copy(email = v, error = null) }
    fun setPassword(v: String) = update { it.copy(password = v, error = null) }

    fun login() {
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password


        if (email.isBlank() || password.isBlank()) {
            update { it.copy(error = "Enter email and password") }
            return
        }

        viewModelScope.launch {
            update { it.copy(isLoading = true, error = null) }
            try {
                repo.login(email, password)
                update { it.copy(isLoading = false, success = true) }
            } catch (e: Exception) {
                val msg = when (e) {
                    is FirebaseAuthInvalidUserException,
                    is FirebaseAuthInvalidCredentialsException -> INVALID_LOGIN_MESSAGE
                    else -> "Login failed. Please try again."
                }

                update { it.copy(isLoading = false, error = msg) }
            }

        }
    }

    fun consumeSuccess() = update { it.copy(success = false) }

    private fun update(block: (LoginUiState) -> LoginUiState) {
        _uiState.value = block(_uiState.value)
    }
}
