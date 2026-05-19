package com.example.weathersnap.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.weathersnap.domain.model.WeatherSnapshot
import com.example.weathersnap.ui.theme.WeatherSnapTheme
import org.junit.Rule
import org.junit.Test

class WeatherCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun weatherCard_displaysCorrectData() {
        val weather = WeatherSnapshot(
            cityName = "London",
            temperature = 20.0,
            condition = "Clear sky",
            humidity = 50,
            windSpeed = 10.0,
            pressure = 1012.0
        )

        composeTestRule.setContent {
            WeatherSnapTheme {
                WeatherCard(weather = weather)
            }
        }

        composeTestRule.onNodeWithText("London").assertIsDisplayed()
        composeTestRule.onNodeWithText("20.0°C").assertIsDisplayed()
        composeTestRule.onNodeWithText("Clear sky").assertIsDisplayed()
        composeTestRule.onNodeWithText("50%").assertIsDisplayed()
    }
}
