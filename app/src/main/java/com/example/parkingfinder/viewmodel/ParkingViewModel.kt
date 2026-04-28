/*
package com.example.parkingfinder.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.parkingfinder.model.ParkingSpace
import com.google.firebase.database.*

class ParkingViewModel : ViewModel() {


    */
/**
     * Reference to the "parkingSpaces" node in Firebase Realtime Database
     *//*

    private val databaseRef: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("parkingSpaces")

    */
/**
     * Holds all parking spaces loaded from Firebase.
     * mutableStateListOf makes Compose automatically recompose
     * whenever items are added/removed.
     *//*

    private val _parkingSpaces = mutableStateListOf<ParkingSpace>()
    val parkingSpaces: List<ParkingSpace> = _parkingSpaces

    */
/**
     * Holds the currently selected parking space (or null if none selected)
     * mutableStateOf allows Compose to react to selection changes.
     *//*

    val selectedParking = mutableStateOf<ParkingSpace?>(null)

    init {
        // Start listening to Firebase as soon as the ViewModel is created
        loadParkingSpaces()
    }

    */
/**
     * Reads parking spaces from Firebase and keeps them in sync in real time.
     * Any change in Firebase automatically updates the UI.
     *//*

    private fun loadParkingSpaces() {
        databaseRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                _parkingSpaces.clear()

                snapshot.children.forEach { child ->
                    val space = child.getValue(ParkingSpace::class.java)
                    space?.let { _parkingSpaces.add(it) }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // You can log or handle database errors here if needed
            }
        })
    }

    */
/**
     * Adds a new parking space to Firebase.
     * Firebase will automatically update all users listening to this node.
     *//*

    fun addParkingSpace(space: ParkingSpace) {
        databaseRef
            .child(space.id)
            .setValue(space)
    }

    */
/**
     * Sets the selected parking space when the user taps a marker
     * or selects a space through search.
     *//*

    fun selectParking(space: ParkingSpace?) {
        selectedParking.value = space
    }

    */
/**
     * Searches for a parking space by Location ID.
     * If found, it becomes the selected parking space.
     *//*

    fun searchByLocationId(locationId: String) {
        val match = _parkingSpaces.firstOrNull {
            it.id.equals(locationId.trim(), ignoreCase = true)
        }

        selectedParking.value = match
    }
}
*/
package com.example.parkingfinder.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.parkingfinder.model.ParkingSpace
import javax.inject.Inject

@HiltViewModel
class ParkingViewModel @Inject constructor() : ViewModel() {

    // List of all parking spaces
    private val _parkingSpaces = MutableStateFlow<List<ParkingSpace>>(emptyList())
    val parkingSpaces: StateFlow<List<ParkingSpace>> = _parkingSpaces

    // Currently selected parking space
    private val _selectedParking = MutableStateFlow<ParkingSpace?>(null)
    val selectedParking: StateFlow<ParkingSpace?> = _selectedParking

    // Select a parking space
    fun selectParking(parkingSpace: ParkingSpace?) {
        _selectedParking.value = parkingSpace
    }

    // Add a new parking space
    fun addParkingSpace(space: ParkingSpace) {
        _parkingSpaces.value = _parkingSpaces.value + space
    }

    // Search by Location ID (just example, implement your logic)
    fun searchByLocationId(id: String) {
        // Example: just filter existing spaces
        val found = _parkingSpaces.value.find { it.id == id }
        _selectedParking.value = found
    }
}
