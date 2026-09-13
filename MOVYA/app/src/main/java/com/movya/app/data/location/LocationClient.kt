package com.movya.app.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.movya.app.domain.model.UserLocation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationClient(
    private val context: Context,
    private val client: FusedLocationProviderClient
) {
    @SuppressLint("MissingPermission")
    fun getLocationUpdates(intervalMs: Long = 5000L): Flow<UserLocation> = callbackFlow {
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, intervalMs)
            .setMinUpdateIntervalMillis(2000L)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                result.lastLocation?.let { location ->
                    trySend(UserLocation(location.latitude, location.longitude))
                }
            }
        }

        client.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())

        awaitClose {
            client.removeLocationUpdates(locationCallback)
        }
    }
}
