package com.ehve.newsweather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ehve.newsweather.domain.model.NewsArticle
import com.ehve.newsweather.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private val _articles = MutableStateFlow<List<NewsArticle>>(emptyList())

    val articles: StateFlow<List<NewsArticle>> = _articles.asStateFlow()

    init {
        fetchTopHeadlines()
    }
    fun fetchTopHeadlines() {
            viewModelScope.launch {
                val result = repository.getTopHeadlines()

                result.onSuccess { list ->
                    _articles.value = list
                }.onFailure {
                    // Handle error
                }
            }
        }
    }
