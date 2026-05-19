package com.example.weathersnap.data.mapper

import com.example.weathersnap.data.remote.dto.CityDto
import com.example.weathersnap.data.remote.dto.WeatherResponseDto
import com.example.weathersnap.domain.model.City
import com.example.weathersnap.domain.model.WeatherSnapshot

fun CityDto.toCity(): City {
    return City(
        id = id,
        name = name,
        latitude = latitude,
        longitude = longitude,
        country = country,
        admin1 = admin1
    )
}

fun WeatherResponseDto.toWeatherSnapshot(cityName: String): WeatherSnapshot {
    val current = current
    return WeatherSnapshot(
        cityName = cityName,
        temperature = current?.temperature ?: 0.0,
        condition = parseWeatherCode(current?.weatherCode ?: 0),
        humidity = current?.humidity ?: 0,
        windSpeed = current?.windSpeed ?: 0.0,
        pressure = current?.pressure ?: 0.0
    )
}

private fun parseWeatherCode(code: Int): String {
    return when (code) {
        0 -> "Clear sky"
        1, 2, 3 -> "Mainly clear, partly cloudy, and overcast"
        45, 48 -> "Fog"
        51, 53, 55 -> "Drizzle"
        61, 63, 65 -> "Rain"
        71, 73, 75 -> "Snow fall"
        80, 81, 82 -> "Rain showers"
        95 -> "Thunderstorm"
        else -> "Unknown"
    }
}
