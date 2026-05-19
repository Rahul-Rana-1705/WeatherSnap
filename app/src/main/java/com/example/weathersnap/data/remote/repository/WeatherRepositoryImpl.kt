package com.example.weathersnap.data.remote.repository

import com.example.weathersnap.data.mapper.toCity
import com.example.weathersnap.data.mapper.toWeatherSnapshot
import com.example.weathersnap.data.remote.api.GeocodingApi
import com.example.weathersnap.data.remote.api.WeatherApi
import com.example.weathersnap.domain.model.City
import com.example.weathersnap.domain.model.WeatherSnapshot
import com.example.weathersnap.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val geocodingApi: GeocodingApi,
    private val weatherApi: WeatherApi
) : WeatherRepository {

    private val suggestionsCache = mutableMapOf<String, List<City>>()

    override suspend fun searchCity(query: String): Result<List<City>> {
        if (query.length <= 2) return Result.success(emptyList())
        
        suggestionsCache[query]?.let {
            return Result.success(it)
        }

        return try {
            val response = geocodingApi.searchCity(query)
            val cities = response.results?.map { it.toCity() } ?: emptyList()
            suggestionsCache[query] = cities
            Result.success(cities)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWeather(city: City): Result<WeatherSnapshot> {
        return try {
            val response = weatherApi.getForecast(city.latitude, city.longitude)
            Result.success(response.toWeatherSnapshot(city.name))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
