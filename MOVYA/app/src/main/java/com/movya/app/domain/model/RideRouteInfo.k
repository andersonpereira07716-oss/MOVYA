package com.movya.app.domain.model

data class LocationPoint(
    val address: String,
    val latitude: Double,
    val longitude: Double
)

data class RideRouteInfo(
    val origin: LocationPoint,
    val destination: LocationPoint,
    val distanceKm: Double,
    val durationMinutes: Int,
    val availableVehicles: List<VehicleCategory> = emptyList()
)
