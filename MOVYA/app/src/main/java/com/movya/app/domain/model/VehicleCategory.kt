package com.movya.app.domain.model

enum class VehicleType {
    ECONOMY,
    COMFORT,
    MOTO,
    LUXURY
}

data class VehicleCategory(
    val id: String,
    val name: String,
    val description: String,
    val type: VehicleType,
    val basePrice: Double,
    val pricePerKm: Double,
    val pricePerMinute: Double,
    val estimatedTimeMinutes: Int,
    val calculatedPrice: Double = 0.0
)
