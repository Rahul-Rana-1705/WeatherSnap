package com.example.weathersnap.data.remote.repository

import com.example.weathersnap.data.remote.api.GeocodingApi
import com.example.weathersnap.data.remote.api.WeatherApi
import com.example.weathersnap.data.remote.dto.CityDto
import com.example.weathersnap.data.remote.dto.GeocodingResponseDto
import com.example.weathersnap.domain.repository.WeatherRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class WeatherRepositoryTest {

    @Mock
    private lateinit var geocodingApi: GeocodingApi
    @Mock
    private lateinit var weatherApi: WeatherApi

    private lateinit var repository: WeatherRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = WeatherRepositoryImpl(geocodingApi, weatherApi)
    }

    @Test
    fun `searchCity returns empty list when query is short`() = runTest {
        val result = repository.searchCity("ab")
        assertTrue(result.isSuccess)
        assertEquals(0, result.getOrNull()?.size)
    }

    @Test
    fun `searchCity returns cities on successful api call`() = runTest {
        val query = "London"
        val mockDto = CityDto(1, "London", 51.5, -0.12, "UK", "England")
        `when`(geocodingApi.searchCity(query)).thenReturn(GeocodingResponseDto(listOf(mockDto)))

        val result = repository.searchCity(query)
        
        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertEquals("London", result.getOrNull()?.first()?.name)
    }
}
