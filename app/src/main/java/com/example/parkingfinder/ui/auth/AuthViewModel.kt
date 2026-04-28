package com.example.parkingfinder.ui.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun signUp(
        name: String,
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        // 1️⃣ Create user with Firebase Auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val userId = auth.currentUser?.uid
                    if (userId == null) {
                        onResult(false, "User created but UID was null.")
                        return@addOnCompleteListener
                    }

                    // 2️⃣ Save extra user data in Firestore
                    val userData = mapOf(
                        "uid" to userId,
                        "name" to name,
                        "email" to email,
                        "createdAt" to System.currentTimeMillis()
                    )

                    firestore.collection("users")
                        .document(userId)
                        .set(userData)
                        .addOnSuccessListener {
                            onResult(true, null)
                        }
                        .addOnFailureListener { e ->
                            onResult(false, e.message)
                        }

                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }
}