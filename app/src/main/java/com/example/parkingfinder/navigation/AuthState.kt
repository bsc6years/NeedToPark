package com.example.parkingfinder.navigation

sealed class AuthState {
    //loading - firebase is checking the database
    object Loading : AuthState()

    //Authenticated if the user is signed in
    object Authenticated : AuthState()

    //it will show unauthenticated if the user is not signed in
    object Unauthenticated : AuthState()
}