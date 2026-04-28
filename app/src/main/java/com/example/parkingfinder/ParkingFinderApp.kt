package com.example.parkingfinder

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.osmdroid.config.Configuration

@HiltAndroidApp
class ParkingFinderApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize OSMDroid ONCE for the whole app
        Configuration.getInstance().userAgentValue = packageName
    }
}

