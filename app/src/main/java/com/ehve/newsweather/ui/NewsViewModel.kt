package com.ehve.newsweather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ehve.newsweather.data.local.FavManager
import com.ehve.newsweather.domain.model.NewsArticle
import com.ehve.newsweather.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository,
    private val favManager: FavManager
) : ViewModel() {

    private val _news = MutableStateFlow<NewsUiState>(NewsUiState.Loading)

    val articles: StateFlow<NewsUiState> = _news.asStateFlow()

    init {
        fetchTopHeadlines()
    }
    fun fetchTopHeadlines() {
            viewModelScope.launch {
                _news.value = NewsUiState.Loading

                val result = repository.getTopHeadlines()

                result.onSuccess { list ->
                    _news.value = NewsUiState.Success(list)
                }.onFailure { error ->
                    _news.value = NewsUiState.Error(error.message ?: "Unknown error")
                }
            }
    }

    fun toggleFavorite(article: NewsArticle) {
        viewModelScope.launch {
            repository.toggleFavoriteArticle(article)
        }
    }

    fun getIsFavorite(url: String): Flow<Boolean> {
        return repository.isArticleFavorite(url)
    }
}

