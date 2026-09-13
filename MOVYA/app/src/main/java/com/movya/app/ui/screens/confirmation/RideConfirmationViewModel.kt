package com.movya.app.ui.screens.confirmation

import androidx.lifecycle.ViewModel
import com.movya.app.data.repository.RideRepository
import com.movya.app.domain.model.LocationPoint
import com.movya.app.domain.model.RideRouteInfo
import com.movya.app.domain.model.VehicleCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class RideConfirmationUiState(
    val routeInfo: RideRouteInfo? = null,
    val selectedVehicle: VehicleCategory? = null,
    val isBooking: Boolean = false,
    val rideConfirmed: Boolean = false
)

class RideConfirmationViewModel : ViewModel() {

    private val repository = RideRepository()
    private val _uiState = MutableStateFlow(RideConfirmationUiState())
    val uiState: StateFlow<RideConfirmationUiState> = _uiState.asStateFlow()

    fun initRoute(origin: LocationPoint, destination: LocationPoint) {
        val routeInfo = repository.calculateRouteEstimate(origin, destination)
        _uiState.value = _uiState.value.copy(
            routeInfo = routeInfo,
            selectedVehicle = routeInfo.availableVehicles.firstOrNull()
        )
    }

    fun selectVehicle(vehicle: VehicleCategory) {
        _uiState.value = _uiState.value.copy(selectedVehicle = vehicle)
    }

    fun confirmRide() {
        _uiState.value = _uiState.value.copy(isBooking = true)
    }
}
