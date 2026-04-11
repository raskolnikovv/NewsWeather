package com.ehve.newsweather.domain.repository

import com.ehve.newsweather.domain.model.NewsArticle

interface NewsRepository {
    suspend fun getTopHeadlines(): Result<List<NewsArticle>>
}