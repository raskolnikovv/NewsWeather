package com.ehve.newsweather.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Utility functions to map WMO Weather interpretation codes to UI icons and descriptions.
 * Reference: https://open-meteo.com/en/docs
 */
fun getWeatherIcon(code: Int): ImageVector {
    return when (code) {
        0 -> Icons.Default.WbSunny
        1, 2, 3 -> Icons.Default.Cloud
        45, 48 -> Icons.Default.Cloud
        51, 53, 55, 61, 63, 65 -> Icons.Default.WaterDrop
        95, 96, 99 -> Icons.Default.Thunderstorm
        else -> Icons.Default.Cloud
    }
}

fun getWeatherDescription(code: Int): String {
    return when (code) {
        0 -> "Clear Sky"
        1, 2, 3 -> "Partly Cloudy"
        45, 48 -> "Foggy"
        51, 53, 55 -> "Drizzle"
        61, 63, 65 -> "Rainy"
        95, 96, 99 -> "Thunderstorm"
        else -> "Cloudy"
    }
}
