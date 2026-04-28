package com.example.parkingfinder.ui.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.parkingfinder.model.ParkingSpace

@Composable
fun ParkingDetailsScreen(
    parkingSpace: ParkingSpace?,
    onBackClick: () -> Unit,
    onBookClick: (ParkingSpace) -> Unit
) {
    if (parkingSpace == null) return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 12.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Need to Park",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color(0xFF1E2BFF),
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
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = parkingSpace.name,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "Location ID - ${parkingSpace.locationId}",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Price Per Hour: £${parkingSpace.pricePerHour}")
                Text("Rating of Space: ${parkingSpace.rating} Stars")
                Text("Size of Parking Space: ${parkingSpace.size}")

                if (parkingSpace.miscInfo.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = parkingSpace.miscInfo,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onBookClick(parkingSpace) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Book")
                }
            }
        }
    }
}