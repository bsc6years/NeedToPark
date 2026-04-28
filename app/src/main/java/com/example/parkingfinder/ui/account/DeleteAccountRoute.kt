package com.example.parkingfinder.ui.account

import androidx.compose.runtime.*
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun DeleteAccountRoute(
    onBackClick: () -> Unit,
    onDeleteSuccess: () -> Unit,
    onDeleteFailure: (String) -> Unit = {}
) {
    var showPasswordSheet by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    DeleteAccountConfirmScreen(
        onBackClick = onBackClick,
        onDeleteConfirmed = {
            errorMessage = null
            showPasswordSheet = true
        }
    )

    if (showPasswordSheet) {
        DeleteAccountPasswordBottomSheet(
            onDismiss = {
                showPasswordSheet = false
                errorMessage = null
            },
            errorMessage = errorMessage,
            isLoading = isLoading,
            onDeleteConfirmed = { password ->
                val user = FirebaseAuth.getInstance().currentUser
                val email = user?.email
                val uid = user?.uid

                if (user == null || email == null || uid == null) {
                    errorMessage = "No logged-in user found."
                    onDeleteFailure(errorMessage!!)
                    return@DeleteAccountPasswordBottomSheet
                }

                isLoading = true
                errorMessage = null

                val credential = EmailAuthProvider.getCredential(email, password)

                user.reauthenticate(credential)
                    .addOnSuccessListener {
                        FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(uid)
                            .delete()
                            .addOnSuccessListener {
                                user.delete()
                                    .addOnSuccessListener {
                                        isLoading = false
                                        showPasswordSheet = false
                                        onDeleteSuccess()
                                    }
                                    .addOnFailureListener { e ->
                                        isLoading = false
                                        errorMessage =
                                            e.localizedMessage ?: "Failed to delete account."
                                        onDeleteFailure(errorMessage!!)
                                    }
                            }
                            .addOnFailureListener { e ->
                                isLoading = false
                                errorMessage =
                                    e.localizedMessage ?: "Failed to delete Firestore user data."
                                onDeleteFailure(errorMessage!!)
                            }
                    }
                    .addOnFailureListener { e ->
                        isLoading = false
                        errorMessage =
                            e.localizedMessage ?: "Password incorrect. Please try again."
                        onDeleteFailure(errorMessage!!)
                    }
            }
        )
    }
}

/*

the confirm screen stays
pressing Delete Account opens the bottom sheet
the password is used to create EmailAuthProvider credentials
reauthenticate(...) is called first
then Firestore user document is deleted
then user.delete() is called

That directly matches Firebase’s recommended pattern for sensitive operations.


--
this keeps visible profile data from hanging around if the auth delete succeeds afterward.
Long term, consider deleting:
vehicles
bookings
receipts or reviews
other user-linked data

And for a production-grade setup, Firebase’s Delete User Data extension is worth considering.



*/