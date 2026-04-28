package com.example.parkingfinder.ui.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun DeleteAccountSuccessScreen(
    onFinished: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(1500)
        onFinished()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 12.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Need to Park",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color(0xFF1E2BFF),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(120.dp))

        Text(
            text = "Account Successfully Deleted",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
    }
}