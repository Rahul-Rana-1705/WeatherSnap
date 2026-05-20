package com.example.weathersnap.ui.screens.weather

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathersnap.domain.model.City
import com.example.weathersnap.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        checkCameraPermission()
    }

    fun checkCameraPermission() {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        updateCameraPermission(granted)
    }

    fun updateCameraPermission(isGranted: Boolean) {
        _uiState.update { it.copy(isCameraPermissionGranted = isGranted) }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query, error = null) }
        
        searchJob?.cancel()
        if (query.length > 2) {
            searchJob = viewModelScope.launch {
                delay(500) // Debounce
                _uiState.update { it.copy(isLoadingSuggestions = true) }
                repository.searchCity(query)
                    .onSuccess { cities ->
                        _uiState.update { it.copy(suggestions = cities, isLoadingSuggestions = false) }
                    }
                    .onFailure { error ->
                        _uiState.update { it.copy(error = error.message, isLoadingSuggestions = false) }
                    }
            }
        } else {
            _uiState.update { it.copy(suggestions = emptyList()) }
        }
    }

    fun onCitySelected(city: City) {
        _uiState.update { 
            it.copy(
                selectedCity = city, 
                suggestions = emptyList(), 
                searchQuery = city.name,
                isLoadingWeather = true,
                error = null
            ) 
        }
        
        viewModelScope.launch {
            repository.getWeather(city)
                .onSuccess { weather ->
                    _uiState.update { it.copy(weatherSnapshot = weather, isLoadingWeather = false) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(error = error.message, isLoadingWeather = false) }
                }
        }
    }
}
