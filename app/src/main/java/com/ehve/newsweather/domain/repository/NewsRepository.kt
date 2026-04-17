package com.ehve.newsweather.domain.repository

import com.ehve.newsweather.domain.model.NewsArticle
import com.ehve.newsweather.domain.model.WeatherInfo
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getTopHeadlines(): Result<List<NewsArticle>>

    fun isArticleFavorite(url: String): Flow<Boolean>
    suspend fun toggleFavoriteArticle(article: NewsArticle)
    fun getFavorites(): Flow<List<NewsArticle>>
    suspend fun getWeather(lat: Double, lon: Double, city: String): Result<WeatherInfo>
}
