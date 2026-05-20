package com.example.weathersnap

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weathersnap.navigation.Screen
import com.example.weathersnap.ui.screens.camera.CustomCameraScreen
import com.example.weathersnap.ui.screens.report.CreateReportScreen
import com.example.weathersnap.ui.screens.report.CreateReportViewModel
import com.example.weathersnap.ui.screens.savedreports.SavedReportsScreen
import com.example.weathersnap.ui.screens.savedreports.SavedReportsViewModel
import com.example.weathersnap.ui.screens.weather.WeatherScreen
import com.example.weathersnap.ui.screens.weather.WeatherViewModel
import com.example.weathersnap.ui.theme.WeatherSnapTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ ->
        // Handle permission result if needed
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherSnapTheme {
                val navController = rememberNavController()

                LaunchedEffect(Unit) {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                }

                NavHost(
                    navController = navController,
                    startDestination = Screen.Weather.route
                ) {
                    composable(Screen.Weather.route) {
                        val viewModel: WeatherViewModel = hiltViewModel()
                        WeatherScreen(
                            viewModel = viewModel,
                            onNavigateToCreateReport = {
                                val snapshot = viewModel.uiState.value.weatherSnapshot
                                if (snapshot != null) {
                                    navController.currentBackStackEntry?.savedStateHandle?.set("weather", snapshot)
                                    navController.navigate(Screen.CreateReport.route)
                                }
                            },
                            onNavigateToSavedReports = {
                                navController.navigate(Screen.SavedReports.route)
                            }
                        )
                    }

                    composable(Screen.CreateReport.route) {
                        val viewModel: CreateReportViewModel = hiltViewModel()
                        
                        // Handle initial weather injection from navigation
                        val weather = navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.get<com.example.weathersnap.domain.model.WeatherSnapshot>("weather")
                        
                        LaunchedEffect(weather) {
                            weather?.let { viewModel.setWeather(it) }
                        }

                        // Handle camera result
                        val capturedPath = navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.get<String>("captured_path")
                        
                        LaunchedEffect(capturedPath) {
                            capturedPath?.let { 
                                viewModel.onImageCaptured(it)
                                navController.currentBackStackEntry?.savedStateHandle?.remove<String>("captured_path")
                            }
                        }

                        CreateReportScreen(
                            viewModel = viewModel,
                            onNavigateToCamera = {
                                navController.navigate(Screen.CustomCamera.route)
                            },
                            onSaveComplete = {
                                navController.navigate(Screen.SavedReports.route) {
                                    popUpTo(Screen.Weather.route)
                                }
                            },
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable(Screen.CustomCamera.route) {
                        CustomCameraScreen(
                            onImageCaptured = { path ->
                                navController.previousBackStackEntry?.savedStateHandle?.set("captured_path", path)
                                navController.popBackStack()
                            },
                            onClose = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable(Screen.SavedReports.route) {
                        val viewModel: SavedReportsViewModel = hiltViewModel()
                        SavedReportsScreen(
                            viewModel = viewModel,
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}
