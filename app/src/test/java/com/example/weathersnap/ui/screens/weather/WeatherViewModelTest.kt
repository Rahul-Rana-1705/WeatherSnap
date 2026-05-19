package com.example.weathersnap.ui.screens.weather

import com.example.weathersnap.domain.model.City
import com.example.weathersnap.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    @Mock
    private lateinit var repository: WeatherRepository
    private lateinit var viewModel: WeatherViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = WeatherViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onSearchQueryChange updates query state immediately`() = runTest {
        viewModel.onSearchQueryChange("London")
        assertEquals("London", viewModel.uiState.value.searchQuery)
    }

    @Test
    fun `onSearchQueryChange triggers repository search after delay`() = runTest {
        val cities = listOf(City(1, "London", 0.0, 0.0, "UK"))
        `when`(repository.searchCity("London")).thenReturn(Result.success(cities))

        viewModel.onSearchQueryChange("London")
        
        // Advance time for debounce (500ms)
        testDispatcher.scheduler.advanceTimeBy(600)
        
        assertEquals(cities, viewModel.uiState.value.suggestions)
    }
}
