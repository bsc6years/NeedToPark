package com.example.parkingfinder.viewmodel

import androidx.lifecycle.ViewModel
import com.example.parkingfinder.navigation.AuthState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

//ViewModel will listen to the Firebase Authentication changes
//It will check if a user exists and then will update it to either authenticated or unauthenticated

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    private val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        _authState.value =
            if (firebaseAuth.currentUser != null) {
                AuthState.Authenticated
            } else {
                AuthState.Unauthenticated
            }
    }

    init {
        auth.addAuthStateListener(authListener)
    }

    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener(authListener)
    }
}