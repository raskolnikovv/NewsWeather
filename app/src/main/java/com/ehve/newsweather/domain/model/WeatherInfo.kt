package com.ehve.newsweather.domain.model


data class WeatherInfo(
    val temperature: Double,
    val weatherCode: Int,
    val city: String,
)