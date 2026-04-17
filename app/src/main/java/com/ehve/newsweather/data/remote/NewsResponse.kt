package com.ehve.newsweather.data.remote

import com.ehve.newsweather.domain.model.NewsArticle

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<NewsArticleDTO>
)

data class NewsArticleDTO(
    val title: String,
    val description: String?,
    val urlToImage: String?,
    val url: String
)

/**
 * Extension function to map DTO to Domain model.
 */
fun NewsArticleDTO.toDomain(): NewsArticle {
    return NewsArticle(
        title = this.title,
        description = this.description ?: "No description available",
        urlToImage = this.urlToImage ?: "",
        url = this.url
    )
}
