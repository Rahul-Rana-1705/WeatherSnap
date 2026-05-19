package com.example.weathersnap.domain.repository

import com.example.weathersnap.domain.model.City
import com.example.weathersnap.domain.model.WeatherSnapshot

interface WeatherRepository {
    suspend fun searchCity(query: String): Result<List<City>>
    suspend fun getWeather(city: City): Result<WeatherSnapshot>
}
