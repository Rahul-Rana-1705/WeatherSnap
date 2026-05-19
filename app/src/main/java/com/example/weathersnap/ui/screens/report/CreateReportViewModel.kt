package com.example.weathersnap.ui.screens.report

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathersnap.data.local.entity.ReportEntity
import com.example.weathersnap.domain.model.WeatherSnapshot
import com.example.weathersnap.domain.repository.ReportRepository
import com.example.weathersnap.utils.ImageCompressor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class CreateReportUiState(
    val weather: WeatherSnapshot? = null,
    val notes: String = "",
    val capturedImagePath: String? = null,
    val originalSize: Long = 0,
    val compressedSize: Long = 0,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false
)

@HiltViewModel
class CreateReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
    private val imageCompressor: ImageCompressor,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReportUiState())
    val uiState: StateFlow<CreateReportUiState> = _uiState.asStateFlow()

    init {
        // Restore from SavedStateHandle if available
        val restoredWeather = savedStateHandle.get<WeatherSnapshot>("weather")
        val restoredNotes = savedStateHandle.get<String>("notes") ?: ""
        val restoredImagePath = savedStateHandle.get<String>("imagePath")
        
        _uiState.update { it.copy(
            weather = restoredWeather,
            notes = restoredNotes,
            capturedImagePath = restoredImagePath
        ) }
    }

    fun setWeather(weather: WeatherSnapshot) {
        _uiState.update { it.copy(weather = weather) }
        savedStateHandle["weather"] = weather
    }

    fun onNotesChange(notes: String) {
        _uiState.update { it.copy(notes = notes) }
        savedStateHandle["notes"] = notes
    }

    fun onImageCaptured(path: String) {
        val originalFile = File(path)
        val originalSize = originalFile.length()
        
        viewModelScope.launch {
            val compressedFile = imageCompressor.compressImage(path)
            _uiState.update { 
                it.copy(
                    capturedImagePath = compressedFile?.absolutePath ?: path,
                    originalSize = originalSize,
                    compressedSize = compressedFile?.length() ?: 0
                ) 
            }
            savedStateHandle["imagePath"] = compressedFile?.absolutePath ?: path
        }
    }

    fun saveReport() {
        val state = uiState.value
        val weather = state.weather ?: return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val report = ReportEntity(
                city = weather.cityName,
                temperature = weather.temperature,
                humidity = weather.humidity,
                pressure = weather.pressure,
                windSpeed = weather.windSpeed,
                condition = weather.condition,
                notes = state.notes,
                imagePath = state.capturedImagePath ?: "",
                originalImageSize = state.originalSize,
                compressedImageSize = state.compressedSize,
                timestamp = System.currentTimeMillis()
            )
            reportRepository.saveReport(report)
            _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
        }
    }
}
