package com.example.weathersnap.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weathersnap.domain.model.WeatherSnapshot

@Composable
fun WeatherSnapHeader(
    title: String,
    subtitle: String,
    buttonText: String? = null,
    onButtonClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFB4C87F),
                            Color(0xFF83A897)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B2B14)
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF1B2B14).copy(alpha = 0.7f)
                )
            }
            if (buttonText != null) {
                Button(
                    onClick = onButtonClick,
                    modifier = Modifier.align(Alignment.TopEnd),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1B2B14),
                        contentColor = Color(0xFFB4C87F)
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = buttonText, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

@Composable
fun WeatherCard(
    weather: WeatherSnapshot,
    modifier: Modifier = Modifier,
    showReportButton: Boolean = false,
    showReadiness: Boolean = false,
    onReportClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = weather.cityName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = weather.condition,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                TemperatureDisplay(temp = weather.temperature)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                WeatherDetailBox(
                    label = "Humidity",
                    value = "${weather.humidity}%",
                    valueColor = Color(0xFF4CAF50).copy(alpha = 0.6f),
                    modifier = Modifier.weight(1f)
                )
                WeatherDetailBox(
                    label = "Wind",
                    value = "${weather.windSpeed} m/s",
                    valueColor = Color(0xFF4285F4).copy(alpha = 0.6f),
                    modifier = Modifier.weight(1f)
                )
                WeatherDetailBox(
                    label = "Pressure",
                    value = "${weather.pressure}",
                    valueColor = Color(0xFFFFA000).copy(alpha = 0.6f),
                    modifier = Modifier.weight(1f)
                )
            }

            if (showReadiness) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF25271F), RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Report readiness",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Camera and Room DB enabled",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            if (showReportButton) {
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onReportClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFC4D68A),
                        contentColor = Color(0xFF1B2B14)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Create Report", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun TemperatureDisplay(temp: Double) {
    Surface(
        color = Color(0xFF343B1D),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = "${temp.toInt()}°C",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFC4D68A)
        )
    }
}

@Composable
fun WeatherDetailBox(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color(0xFF23261F),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
        }
    }
}
