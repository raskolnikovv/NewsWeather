package com.ehve.newsweather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ehve.newsweather.data.local.FavManager
import com.ehve.newsweather.domain.model.NewsArticle
import com.ehve.newsweather.domain.model.WeatherInfo
import com.ehve.newsweather.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing news headlines and weather state.
 */
@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository,
    private val favManager: FavManager
) : ViewModel() {

    private val _news = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val articles: StateFlow<NewsUiState> = _news.asStateFlow()

    private val _weather = MutableStateFlow<WeatherInfo?>(null)
    val weather: StateFlow<WeatherInfo?> = _weather.asStateFlow()

    init {
        fetchTopHeadlines()
    }

    /**
     * Fetches top headlines and updates [articles] state.
     */
    fun fetchTopHeadlines() {
        viewModelScope.launch {
            _news.value = NewsUiState.Loading
            repository.getTopHeadlines()
                .onSuccess { _news.value = NewsUiState.Success(it) }
                .onFailure { _news.value = NewsUiState.Error(it.message ?: "Unknown error") }
        }
    }

    /**
     * Updates weather state for the given coordinates and city name.
     */
    fun fetchWeather(lat: Double, lon: Double, city: String) {
        viewModelScope.launch {
            repository.getWeather(lat, lon, city)
                .onSuccess { _weather.value = it }
                .onFailure { _weather.value = null }
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
