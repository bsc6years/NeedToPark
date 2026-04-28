package com.example.parkingfinder.ui.account

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DeleteAccountConfirmScreen(
    onBackClick: () -> Unit,
    onDeleteConfirmed: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 12.dp),
        verticalArrangement = Arrangement.Top
    ) {
        TextButton(onClick = onBackClick) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Need to Park",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color(0xFF1E2BFF),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(80.dp))

        Text(
            text = "Are you sure you want to delete account?",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Once your account is deleted you will not be able to retrieve your account and data will not be able to be recovered.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onDeleteConfirmed,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            )
        ) {
            Text("Delete Account")
        }
    }
}