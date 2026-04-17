package com.ehve.newsweather.domain.repository

import com.ehve.newsweather.domain.model.NewsArticle
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getTopHeadlines(): Result<List<NewsArticle>>

    fun isArticleFavorite(url: String): Flow<Boolean>
    suspend fun toggleFavoriteArticle(article: NewsArticle)
    fun getFavorites(): Flow<List<NewsArticle>>
}
