package com.example.parkingfinder.ui.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun AccountScreen(
    onLogoutConfirmed: () -> Unit,
    onProfileClick: () -> Unit,
    onVehiclesClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    // Controls whether the confirmation dialog is visible
    //false = dialogue is not visible, true = dialogue is visible
    //remember keeps the value while composable is active
    var showLogoutDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top / middle content
            Column {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Need to Park",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Put your profile / vehicles / receipts / notifications cards here
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AccountOptionCard(
                        title = "Profile",
                        modifier = Modifier.weight(1f),
                        onClick = onProfileClick
                    )

                    AccountOptionCard(
                        title = "Vehicles",
                        modifier = Modifier.weight(1f),
                        onClick = onVehiclesClick
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AccountOptionCard(
                        title = "Receipts",
                        modifier = Modifier.weight(1f),
                        onClick = { }
                    )

                    AccountOptionCard(
                        title = "Notifications",
                        modifier = Modifier.weight(1f),
                        onClick = { }
                    )
                }
                //Code below was made by me
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AccountOptionCard(
                        title = "Beta Features",
                        modifier = Modifier.weight(1f),
                        onClick = { }
                    )
                    /*//The bellow doesnt make the beta features fill the row, you can take it away and add another button if needed.
                    Spacer(modifier = Modifier.weight(1f))*/
                    AccountOptionCard(
                        title = "Settings",
                        modifier = Modifier.weight(1f),
                        onClick = onSettingsClick
                    )
                }


                // Keep your existing account page content here
            }

            // Bottom logout button
            Button(
                //by clicking the button it does not log user out. it just changes the state variable
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors()
            ) {
                Text(text = "Log Out")
            }
        }
    }

    // Confirmation dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(text = "Confirm Logout")
            },
            text = {
                Text(text = "Are you sure you want to sign out?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogoutConfirmed()
                    }
                ) {
                    Text("Log Out")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Composable
fun AccountOptionCard(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(90.dp)
            .background(
                color = androidx.compose.ui.graphics.Color(0xFFEDE3F8),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

//This function allows to see the screen in the preivew mode
@Preview(showBackground = true)
@Composable
fun AccountScreenPreview() {
    AccountScreen(
        onLogoutConfirmed = {},
        onProfileClick = {},
        onVehiclesClick = {},
        onSettingsClick = {}
    )
}