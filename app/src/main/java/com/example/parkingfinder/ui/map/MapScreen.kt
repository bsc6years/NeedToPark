package com.example.parkingfinder.ui.map

import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.parkingfinder.R
import com.example.parkingfinder.model.ParkingSpace
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import androidx.compose.ui.platform.LocalFocusManager

//following imports are used for the location services and to remember the options selected
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import com.example.parkingfinder.data.SettingsManager
import com.google.android.gms.location.LocationServices

import android.widget.Toast
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.OutlinedTextFieldDefaults


@Composable
fun MapScreen(
    parkingSpaces: List<ParkingSpace>,
    selectedParking: ParkingSpace?,
    onParkingClicked: (ParkingSpace) -> Unit,
    onSearchById: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val settingsManager = remember { SettingsManager(context) }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var locationServicesEnabled by remember {
        mutableStateOf(settingsManager.isLocationServicesEnabled())
    }

    // Remember MapView once
    val mapView = remember {
        MapView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(15.0)
            controller.setCenter(GeoPoint(51.53629, -0.15706)) // London example
        }
    }

    //permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted && locationServicesEnabled) {
            if (
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        mapView.controller.animateTo(GeoPoint(it.latitude, it.longitude))
                        mapView.controller.setZoom(16.0)
                    }
                }
            }
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    var searchQuery by remember { mutableStateOf("") }



    LaunchedEffect(Unit) {
        locationServicesEnabled = settingsManager.isLocationServicesEnabled()
    }

    // Lifecycle
    DisposableEffect(lifecycleOwner, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDetach()
        }
    }

    // Move camera when selecting a space
    LaunchedEffect(selectedParking?.id) {
        selectedParking?.let {
            mapView.controller.animateTo(GeoPoint(it.latitude, it.longitude))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp)) // 👈 pushes search bar down
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { newValue ->
                searchQuery = newValue

                // ✅ If user clears the text, reset immediately (no need to press enter)
                if (newValue.isBlank()) {
                    onSearchById("")
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus() // avoids weird keyboard/UI behaviour
                    onSearchById(searchQuery.trim())
                }
            ),
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            searchQuery = ""
                            focusManager.clearFocus()
                            onSearchById("") // reset results
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Clear search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            placeholder = {
                Text(
                    text = "Enter Location ID",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,

                focusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.60f),

                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,

                focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,

                focusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Map container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp)
                .background(Color.LightGray, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { mapView },
                update = { map ->

                    // Remove only markers (keeps other overlays if you add any later)
                    map.overlays.removeAll { it is Marker }

                    parkingSpaces.forEach { space ->
                        val marker = Marker(map).apply {
                            position = GeoPoint(space.latitude, space.longitude)
                            title = space.name
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                            icon =
                                if (space.id == selectedParking?.id)
                                    ContextCompat.getDrawable(context, R.drawable.ic_selected_pin)
                                else
                                    ContextCompat.getDrawable(context, R.drawable.ic_pin)

                            setOnMarkerClickListener { _, _ ->
                                onParkingClicked(space)
                                true
                            }
                        }
                        map.overlays.add(marker)
                    }
                    map.invalidate()
                }

            )

            FloatingActionButton(
                onClick = {
                    locationServicesEnabled = settingsManager.isLocationServicesEnabled()
                    // Will prompt the user to turn on Location Services in the app. It is called Toast
                    if (!locationServicesEnabled) {
                        Toast.makeText(
                            context,
                            "To use Location Services, it must be turned on in the app's Settings.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@FloatingActionButton
                    }

                    val permissionGranted =
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED

                    if (permissionGranted) {
                        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                            location?.let {
                                mapView.controller.animateTo(GeoPoint(it.latitude, it.longitude))
                                mapView.controller.setZoom(16.0)
                            }
                        }
                    } else {
                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                },
                modifier = Modifier
                    .align(androidx.compose.ui.Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = Color.White,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Filled.MyLocation,
                    contentDescription = "Go to my location"
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(72.dp)) // 👈 reserved for bottom nav
}
