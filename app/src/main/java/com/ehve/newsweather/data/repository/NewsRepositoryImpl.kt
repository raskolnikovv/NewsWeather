package com.ehve.newsweather.data.repository

import com.ehve.newsweather.BuildConfig
import com.ehve.newsweather.data.local.FavManager
import com.ehve.newsweather.data.remote.NewsApiService
import com.ehve.newsweather.data.remote.toDomain
import com.ehve.newsweather.domain.model.NewsArticle
import com.ehve.newsweather.domain.model.WeatherInfo
import com.ehve.newsweather.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApiService,
    private val favManager: FavManager
) : NewsRepository {

    override suspend fun getTopHeadlines(): Result<List<NewsArticle>> {
        return try {
            val response = api.getTopHeadlines(apiKey = BuildConfig.NEWS_API_KEY)
            val articles = response.articles.map { it.toDomain() }
            Result.success(articles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun isArticleFavorite(url: String): Flow<Boolean> {
        return favManager.favoritesFlow.map { articles ->
            articles.any { it.url == url }
        }
    }

    override suspend fun toggleFavoriteArticle(article: NewsArticle) {
        val currentFavorites = favManager.favoritesFlow.first().toMutableList()
        val isFav = currentFavorites.any { it.url == article.url }

        if (isFav) {
            currentFavorites.removeAll { it.url == article.url }
        } else {
            currentFavorites.add(article)
        }

        favManager.saveFavorites(currentFavorites)
    }

    override fun getFavorites(): Flow<List<NewsArticle>> {
        return favManager.favoritesFlow
    }

    override suspend fun getWeather(lat: Double, lon: Double, city: String): Result<WeatherInfo> {
        return try {
            val url = "https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$lon&current_weather=true"
            val response = api.getWeather(url)
            val info = WeatherInfo(
                temperature = response.currentWeather.temperature,
                weatherCode = response.currentWeather.weathercode,
                city = city
            )
            Result.success(info)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
