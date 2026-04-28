package com.example.parkingfinder.data

/*What this page does
creates a small local settings store
saves whether location services are enabled
keeps it even after the app closes*/


import android.content.Context

class SettingsManager(context: Context) {

    private val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LOCATION_SERVICES = "location_services"
    }

    fun isLocationServicesEnabled(): Boolean {
        return prefs.getBoolean(KEY_LOCATION_SERVICES, false)
    }

    fun setLocationServicesEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_LOCATION_SERVICES, enabled).apply()
    }
}