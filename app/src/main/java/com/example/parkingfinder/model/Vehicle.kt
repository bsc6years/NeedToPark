package com.example.parkingfinder.model

data class Vehicle(
    val id: String = "",

    // Links the vehicle to the logged-in user
    val userId: String = "",

    // Main vehicle details
    val registration: String = "",
    val make: String = "",
    val model: String = "",
    val colour: String = "",

    // Useful metadata for sorting later if needed
    val createdAtMillis: Long = 0L
)