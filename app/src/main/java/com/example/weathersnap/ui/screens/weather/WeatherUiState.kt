package com.example.weathersnap.ui.screens.weather

import com.example.weathersnap.domain.model.City
import com.example.weathersnap.domain.model.WeatherSnapshot

data class WeatherUiState(
    val searchQuery: String = "",
    val suggestions: List<City> = emptyList(),
    val isLoadingSuggestions: Boolean = false,
    val selectedCity: City? = null,
    val weatherSnapshot: WeatherSnapshot? = null,
    val isLoadingWeather: Boolean = false,
    val isCameraPermissionGranted: Boolean = false,
    val error: String? = null
)
