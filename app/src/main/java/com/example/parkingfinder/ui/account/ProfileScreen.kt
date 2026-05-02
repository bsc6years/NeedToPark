package com.example.parkingfinder.ui.account


import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.material3.Surface
import androidx.compose.ui.text.font.FontWeight


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import androidx.compose.material3.OutlinedTextFieldDefaults




@Composable
fun ProfileScreen(
    name: String,
    email: String,
    onSaveClick: () -> Unit,
    onDeleteAccountClick: () -> Unit
) {

    var showUpdatePasswordSheet by remember { mutableStateOf(false) }
    var showPasswordUpdatedMessage by remember { mutableStateOf(false) }

    var showUpdateNameSheet by remember { mutableStateOf(false) }
    var showNameUpdatedMessage by remember { mutableStateOf(false) }
    var currentNameState by remember(name) { mutableStateOf(name) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Need to Park",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color(0xFF3D33FF),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                //Previous Name Field - For Report Writting
                /*
                Text(text = "Name")
                OutlinedTextField(
                    value = name,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )
                */

                Text(text = "Name")

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showUpdateNameSheet = true
                        },
                    shape = RoundedCornerShape(8.dp),
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp
                ) {
                    OutlinedTextField(
                        value = currentNameState,
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        enabled = false
                    )
                }

                if (showNameUpdatedMessage) {
                    Text(
                        text = "Name Updated",
                        color = Color(0xFF2E7D32),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Text(text = "Email")
                OutlinedTextField(
                    value = email,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )

                Text(text = "Update Password")

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showUpdatePasswordSheet = true
                        },
                    shape = RoundedCornerShape(8.dp),
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp
                ) {
                    OutlinedTextField(
                        value = "************",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        enabled = false
                    )
                }

                if (showUpdateNameSheet) {
                    UpdateNameBottomSheet(
                        currentName = currentNameState,
                        onDismiss = {
                            showUpdateNameSheet = false
                        },
                        onNameUpdated = { updatedName ->
                            currentNameState = updatedName
                            showUpdateNameSheet = false
                            showNameUpdatedMessage = true
                        }
                    )
                }


                if (showPasswordUpdatedMessage) {
                    Text(
                        text = "Password Updated",
                        color = Color(0xFF2E7D32),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                //Previous Update Password Field - For Report Writting
                /*
                Text(text = "Update Password")
                OutlinedTextField(
                    value = "************",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )
                */

                Button(
                    onClick = onSaveClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                    /*
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4B2FFF)
                    )
                    */
                ) {
                    Text("Save")
                }
            }
        }

        Spacer(modifier = Modifier.height(110.dp))

        Button(
            onClick = onDeleteAccountClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            )
        ) {
            Text(
                text = "Delete account",
                fontSize = 30.sp
            )

        }
    }
    if (showUpdatePasswordSheet) {
        UpdatePasswordBottomSheet(
            onDismiss = {
                showUpdatePasswordSheet = false
            },
            onPasswordUpdated = {
                showUpdatePasswordSheet = false
                showPasswordUpdatedMessage = true
            }
        )
    }
}

//The following code was updates as name wasn't showing on the proife screen
/*
@Composable
fun ProfileRoute(
    onSaveClick: () -> Unit = {},
    onDeleteAccountClick: () -> Unit = {}
) {
    val currentUser = FirebaseAuth.getInstance().currentUser

    val email = currentUser?.email ?: "No email found"
    val name = currentUser?.displayName ?: "No name found"

    */

@Composable
fun ProfileRoute(
    onSaveClick: () -> Unit = {},
    onDeleteAccountClick: () -> Unit = {}
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val firestore = FirebaseFirestore.getInstance()

    var name by remember { mutableStateOf("Loading...") }
    val email = currentUser?.email ?: "No email found"

    LaunchedEffect(currentUser?.uid) {
        val uid = currentUser?.uid
        if (uid != null) {
            firestore.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->
                    name = document.getString("name") ?: "No name found"
                }
                .addOnFailureListener {
                    name = "No name found"
                }
        } else {
            name = "No name found"
        }
    }

    ProfileScreen(
        name = name,
        email = email,
        onSaveClick = onSaveClick,
        onDeleteAccountClick = onDeleteAccountClick
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        name = "Testing Apps",
        email = "TA@TestingAppsAndARandomEmail.com",
        onSaveClick = {},
        onDeleteAccountClick = {}
    )
}

