package com.ehve.newsweather.data.repository

import com.ehve.newsweather.BuildConfig
import com.ehve.newsweather.data.local.FavManager
import com.ehve.newsweather.data.remote.NewsApiService
import com.ehve.newsweather.data.remote.toDomain
import com.ehve.newsweather.domain.model.NewsArticle
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

    // Verifica se a URL da notícia está na lista de favoritos
    override fun isArticleFavorite(url: String): Flow<Boolean> {
        return favManager.favoritesFlow.map { articles ->
            articles.any { it.url == url }
        }
    }

    // Lógica para adicionar ou remover dos favoritos
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
}