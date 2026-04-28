package com.example.parkingfinder.data

import com.example.parkingfinder.model.ParkingSpace
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ParkingRepository(
    private val db: FirebaseFirestore
) {

    fun observeParkingSpaces(): Flow<List<ParkingSpace>> = callbackFlow {
        val registration: ListenerRegistration =
            db.collection("parkingSpaces")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        // This will be caught by the ViewModel try/catch
                        close(error)
                        return@addSnapshotListener
                    }

                    val spaces = snapshot?.documents
                        ?.mapNotNull { doc ->
                            try {
                                val locationId = doc.getString("locationId") ?: return@mapNotNull null
                                val name = doc.getString("name") ?: return@mapNotNull null
                                val latitude = doc.getDouble("latitude") ?: return@mapNotNull null
                                val longitude = doc.getDouble("longitude") ?: return@mapNotNull null

                                val price = doc.getDouble("pricePerHour") ?: 0.0
                                val size = doc.getString("size") ?: "Unknown"
                                val rating = doc.getDouble("rating") ?: 0.0
                                val miscInfo = doc.getString("miscInfo") ?: ""

                                ParkingSpace(
                                    id = doc.id,
                                    locationId = locationId,
                                    name = name,
                                    latitude = latitude,
                                    longitude = longitude,
                                    pricePerHour = price,
                                    size = size,
                                    rating = rating,
                                    miscInfo = miscInfo
                                )
                            } catch (e: Exception) {
                                // Skip malformed docs instead of killing the stream
                                null
                            }
                        }
                        .orEmpty()

                    // Send latest list to collectors
                    trySend(spaces).isSuccess
                }

        awaitClose { registration.remove() }
    }
}
