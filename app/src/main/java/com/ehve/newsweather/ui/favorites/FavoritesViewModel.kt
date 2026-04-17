package com.ehve.newsweather.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ehve.newsweather.domain.model.NewsArticle
import com.ehve.newsweather.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel for managing user's favorite news articles.
 */
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    val favoriteArticles: StateFlow<List<NewsArticle>> = repository.getFavorites()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
