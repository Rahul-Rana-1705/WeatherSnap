package com.example.weathersnap.ui.screens.weather

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.weathersnap.ui.components.WeatherCard
import com.example.weathersnap.ui.components.WeatherSnapHeader

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    onNavigateToCreateReport: () -> Unit,
    onNavigateToSavedReports: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

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
                            onClick = { /* Search is automatic but button for UI */ },
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
