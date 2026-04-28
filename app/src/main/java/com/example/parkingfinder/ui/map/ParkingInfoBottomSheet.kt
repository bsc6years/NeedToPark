package com.example.parkingfinder.ui.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.parkingfinder.model.ParkingSpace

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParkingInfoBottomSheet(
    parkingSpace: ParkingSpace?,
    onDismiss: () -> Unit,
    onBookClick: (ParkingSpace) -> Unit,
    onMoreInfoClick: (ParkingSpace) -> Unit
) {
    if (parkingSpace == null) return

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Title + subtitle (like your Figma)
            Text(
                text = parkingSpace.name,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Location ID: ${parkingSpace.locationId}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Details
            Text(
                text = "Details",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Price: £${parkingSpace.pricePerHour}/hour")
            Text("Size: ${parkingSpace.size}")
            Text("Rating: ${parkingSpace.rating} ⭐ (Beta Feature, Under Construction)")

            // Optional extra text to store miscInfo such as the parking time limits of the space
            // or maximum stay of the space. This is to be editied later
            if (parkingSpace.miscInfo.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = parkingSpace.miscInfo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Buttons row (secondary + primary), matches Figma design
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { onMoreInfoClick(parkingSpace) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("More Info")
                }

                Button(
                    onClick = { onBookClick(parkingSpace) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Book Now")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}


