package com.ehve.newsweather.data.repository

import com.ehve.newsweather.BuildConfig
import com.ehve.newsweather.data.remote.NewsApiService
import com.ehve.newsweather.data.remote.toDomain
import com.ehve.newsweather.domain.model.NewsArticle
import com.ehve.newsweather.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApiService
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
}