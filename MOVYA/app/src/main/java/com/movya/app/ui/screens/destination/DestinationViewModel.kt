package com.movya.app.ui.screens.destination

import android.app.Application
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.movya.app.domain.model.LocationPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

data class DestinationUiState(
    val searchQuery: String = "",
    val suggestions: List<LocationPoint> = emptyList(),
    val isLoading: Boolean = false,
    val selectedDestination: LocationPoint? = null
)

class DestinationViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(DestinationUiState())
    val uiState: StateFlow<DestinationUiState> = _uiState.asStateFlow()

    private val geocoder = Geocoder(application.applicationContext, Locale.getDefault())

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        if (query.length >= 3) {
            searchAddresses(query)
        } else {
            _uiState.value = _uiState.value.copy(suggestions = emptyList())
        }
    }

    private fun searchAddresses(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocationName(query, 5)
                val points = addresses?.mapNotNull { address ->
                    val fullAddress = address.getAddressLine(0) ?: "${address.featureName}, ${address.subAdminArea}"
                    LocationPoint(
                        address = fullAddress,
                        latitude = address.latitude,
                        longitude = address.longitude
                    )
                } ?: emptyList()

                _uiState.value = _uiState.value.copy(
                    suggestions = points,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    suggestions = emptyList(),
                    isLoading = false
                )
            }
        }
    }

    fun selectDestination(locationPoint: LocationPoint) {
        _uiState.value = _uiState.value.copy(selectedDestination = locationPoint)
    }
}
