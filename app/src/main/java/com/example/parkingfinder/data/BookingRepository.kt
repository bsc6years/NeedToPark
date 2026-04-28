package com.example.parkingfinder.data

import com.example.parkingfinder.model.Booking
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BookingRepository(
    private val db: FirebaseFirestore
) {
    suspend fun createBooking(booking: Booking): String {
        //docRef is what will name the database in firebase change to bookings if any issues
        val docRef = db.collection("bookings").document() // auto id
        val bookingWithId = booking.copy(id = docRef.id)
        docRef.set(bookingWithId).await()
        return docRef.id
    }
}