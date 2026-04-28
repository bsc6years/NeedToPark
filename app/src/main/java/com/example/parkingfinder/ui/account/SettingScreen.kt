package com.example.parkingfinder.ui.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import androidx.compose.ui.platform.LocalContext
import com.example.parkingfinder.data.SettingsManager

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit = {}
) {
    var appNotificationsOn by remember { mutableStateOf(true) }
    var realTimeNotificationsOn by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val settingsManager = remember { SettingsManager(context) }

    var locationServicesOn by remember {
        mutableStateOf(settingsManager.isLocationServicesEnabled())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 12.dp)
    ) {
        TextButton(
            onClick = onBackClick
        ) {
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

        Spacer(modifier = Modifier.height(40.dp))

        SettingToggleRow(
            title = "App Notifications",
            isOn = appNotificationsOn,
            onToggleChanged = { appNotificationsOn = it }
        )

        Spacer(modifier = Modifier.height(28.dp))

        SettingToggleRow(
            title = "Real Time Parking Notifications",
            isOn = realTimeNotificationsOn,
            onToggleChanged = { realTimeNotificationsOn = it }
        )

        Spacer(modifier = Modifier.height(28.dp))

        SettingToggleRow(
            title = "Location Services",
            isOn = locationServicesOn,
            onToggleChanged = {
                locationServicesOn = it
                settingsManager.setLocationServicesEnabled(it)
            }
        )
    }
}

@Composable
fun SettingToggleRow(
    title: String,
    isOn: Boolean,
    onToggleChanged: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            color = Color(0xFFF7F7F7),
            modifier = Modifier
                .width(118.dp)
                .height(40.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ToggleSegment(
                    text = "On",
                    selected = isOn,
                    onClick = { onToggleChanged(true) },
                    modifier = Modifier.weight(1f)
                )

                ToggleSegment(
                    text = "Off",
                    selected = !isOn,
                    onClick = { onToggleChanged(false) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun ToggleSegment(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(40.dp)
            .background(
                color = if (selected) Color(0xFFE7DDF8) else Color.Transparent,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    MaterialTheme {
        SettingsScreen()
    }
}