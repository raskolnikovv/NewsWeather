package com.ehve.newsweather.domain.model

data class NewsArticle(
    val title: String,
    val description: String,
    val urlToImage: String,
    val url: String,
    val isSaved: Boolean = false
)