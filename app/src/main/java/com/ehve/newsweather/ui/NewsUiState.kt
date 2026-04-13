package com.ehve.newsweather.ui

import com.ehve.newsweather.domain.model.NewsArticle

sealed class NewsUiState {
    object Loading : NewsUiState()
    data class Success(val articles: List<NewsArticle>) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}