package com.movya.app.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.movya.app.data.location.LocationClient
import com.movya.app.domain.model.UserLocation
import com.movya.app.utils.PermissionUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val userLocation: UserLocation? = null,
    val hasLocationPermission: Boolean = false,
    val isCentering: Boolean = false
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val locationClient = LocationClient(
        application.applicationContext,
        LocationServices.getFusedLocationProviderClient(application.applicationContext)
    )

    fun checkPermissionsAndObserveLocation() {
        val hasPermission = PermissionUtils.hasLocationPermission(getApplication())
        _uiState.value = _uiState.value.copy(hasLocationPermission = hasPermission)

        if (hasPermission) {
            viewModelScope.launch {
                locationClient.getLocationUpdates().collect { location ->
                    _uiState.value = _uiState.value.copy(userLocation = location)
                }
            }
        }
    }
}
