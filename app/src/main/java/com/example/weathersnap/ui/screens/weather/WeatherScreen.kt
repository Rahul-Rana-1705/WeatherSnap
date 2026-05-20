package com.example.weathersnap.ui.screens.weather

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.weathersnap.ui.components.WeatherCard
import com.example.weathersnap.ui.components.WeatherSnapHeader

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    onNavigateToCreateReport: () -> Unit,
    onNavigateToSavedReports: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Observe lifecycle to re-check permission when app returns from settings or dialog
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val isGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
                viewModel.updateCameraPermission(isGranted)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WeatherSnapHeader(
                title = "WeatherSnap",
                subtitle = "Live weather reports with camera evidence",
                buttonText = "Reports",
                onButtonClick = onNavigateToSavedReports
            )

            // Search Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = uiState.searchQuery,
                            onValueChange = viewModel::onSearchQueryChange,
                            modifier = Modifier.weight(1f),
                            label = { Text("City") },
                            placeholder = { Text("Search city...") },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )
                        Button(
                            onClick = { /* Search is automatic */ },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFC4D68A),
                                contentColor = Color(0xFF1B2B14)
                            ),
                            modifier = Modifier.height(56.dp).padding(top = 8.dp)
                        ) {
                            Text("Search")
                        }
                    }

                    Text(
                        text = "Enter more than 2 letters to start city suggestions.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (uiState.isLoadingWeather) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }

                    uiState.error?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }

                    AnimatedContent(
                        targetState = uiState.weatherSnapshot,
                        label = "WeatherCard"
                    ) { weather ->
                        if (weather != null) {
                            WeatherCard(
                                weather = weather,
                                showReportButton = true,
                                showReadiness = true,
                                isCameraEnabled = uiState.isCameraPermissionGranted,
                                onReportClick = onNavigateToCreateReport
                            )
                        }
                    }
                }

                // Suggestions Overlay
                if (uiState.suggestions.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .heightIn(max = 250.dp)
                            .zIndex(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF25271F)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        LazyColumn {
                            items(uiState.suggestions) { city ->
                                ListItem(
                                    headlineContent = { Text(city.name, color = Color.White) },
                                    supportingContent = { 
                                        Text(
                                            "${city.admin1 ?: ""}, ${city.country}",
                                            color = Color.White.copy(alpha = 0.6f)
                                        ) 
                                    },
                                    modifier = Modifier.clickable { viewModel.onCitySelected(city) },
                                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
