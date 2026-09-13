package com.movya.app.data.repository

import com.movya.app.domain.model.LocationPoint
import com.movya.app.domain.model.RideRouteInfo
import com.movya.app.domain.model.VehicleCategory
import com.movya.app.domain.model.VehicleType
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class RideRepository {

    private val baseCategories = listOf(
        VehicleCategory(
            id = "moto",
            name = "Movya Moto",
            description = "Agilidade e economia para o seu dia",
            type = VehicleType.MOTO,
            basePrice = 3.50,
            pricePerKm = 1.20,
            pricePerMinute = 0.25,
            estimatedTimeMinutes = 0
        ),
        VehicleCategory(
            id = "economy",
            name = "Movya Econômico",
            description = "Carros compactos com ar-condicionado",
            type = VehicleType.ECONOMY,
            basePrice = 5.00,
            pricePerKm = 1.80,
            pricePerMinute = 0.40,
            estimatedTimeMinutes = 0
        ),
        VehicleCategory(
            id = "comfort",
            name = "Movya Confort",
            description = "Carros mais espaçosos e novos",
            type = VehicleType.COMFORT,
            basePrice = 7.50,
            pricePerKm = 2.40,
            pricePerMinute = 0.55,
            estimatedTimeMinutes = 0
        ),
        VehicleCategory(
            id = "luxury",
            name = "Movya Black / Luxo",
            description = "Sedans premium e atendimento VIP",
            type = VehicleType.LUXURY,
            basePrice = 12.00,
            pricePerKm = 3.50,
            pricePerMinute = 0.85,
            estimatedTimeMinutes = 0
        )
    )

    fun calculateRouteEstimate(origin: LocationPoint, destination: LocationPoint): RideRouteInfo {
        val distanceKm = calculateHaversineDistance(
            origin.latitude, origin.longitude,
            destination.latitude, destination.longitude
        )

        // Estimativa simples de tempo baseada na distância (média de 30 km/h no trânsito urbano)
        val durationMinutes = ((distanceKm / 30.0) * 60.0).toInt().coerceAtLeast(3)

        val categoriesWithPrices = baseCategories.map { category ->
            val price = category.basePrice + (distanceKm * category.pricePerKm) + (durationMinutes * category.pricePerMinute)
            val finalPrice = String.format("%.2f", price).toDouble()
            val categoryEta = (durationMinutes * (if (category.type == VehicleType.MOTO) 0.8 else 1.0)).toInt().coerceAtLeast(2)

            category.copy(
                calculatedPrice = finalPrice,
                estimatedTimeMinutes = categoryEta
            )
        }

        return RideRouteInfo(
            origin = origin,
            destination = destination,
            distanceKm = String.format("%.2f", distanceKm).toDouble(),
            durationMinutes = durationMinutes,
            availableVehicles = categoriesWithPrices
        )
    }

    private fun calculateHaversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0 // Raio da Terra em KM
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }
}
