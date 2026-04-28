package com.example.parkingfinder.model

data class Booking(
    val id: String = "",

    // Auth / ownership
    val userId: String = "",

    // Parking space reference
    val spaceId: String = "",
    val locationId: String = "",
    val spaceName: String = "",

    // Vehicle details
    val vehicleId: String = "",
    val vehicleReg: String = "",
    val vehicleType: String = "Car", // optional but useful

    // Session details
    val startTimeMillis: Long = 0L,
    val durationMinutes: Int = 0,
    val endTimeMillis: Long = 0L,

    // Pricing
    val pricePerHour: Double = 0.0,
    val totalPrice: Double = 0.0,

    // Payment (this depends on the payment provider to be used. either strip or revolut)
    val paymentStatus: String = "unpaid", // unpaid / paid / failed / refunded
    val amount: Double = 0.0,
    val currency: String = "GBP",
    val paymentProvider: String = "",     // "stripe" or "revolut" later
    val paymentRef: String = "",          // paymentIntentId / transactionId later

    // Metadata
    val status: String = "pending",       // pending / active / cancelled / completed
    val createdAtMillis: Long = 0L
)
