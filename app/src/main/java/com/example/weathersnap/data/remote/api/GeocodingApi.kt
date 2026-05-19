package com.example.weathersnap.data.remote.api

import com.example.weathersnap.data.remote.dto.GeocodingResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApi {
    @GET("v1/search")
    suspend fun searchCity(
        @Query("name") name: String,
        @Query("count") count: Int = 10,
        @Query("language") language: String = "en",
        @Query("format") format: String = "json"
    ): GeocodingResponseDto

    companion object {
        const val BASE_URL = "https://geocoding-api.open-meteo.com/"
    }
}
