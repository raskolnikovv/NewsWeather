package com.ehve.newsweather.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Retrofit API definitions for News and Weather services.
 */
interface NewsApiService {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String
    ): NewsResponse

    @GET
    suspend fun getWeather(
        @Url url: String
    ): WeatherResponse
}
