package com.example.parkingfinder.model

data class ParkingSpace(
    val id: String = "",
    val locationId: String = "", //Cashless Identifier
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val pricePerHour: Double = 0.0,
    val size: String = "",
    val rating: Double = 0.0,
    val miscInfo: String = ""
)
