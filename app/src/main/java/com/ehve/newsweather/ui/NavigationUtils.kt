package com.ehve.newsweather.ui

import androidx.navigation.NavController
import com.ehve.newsweather.domain.model.NewsArticle
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Navigation destination constants and helper extensions.
 */
object Routes {
    const val HOME = "home"
    const val FAVORITES = "favorites"
    const val DETAILS = "news_detail/{title}/{url}/{desc}/{imageUrl}"
}

/**
 * Extension function to navigate to details screen with safe URL encoding.
 */
fun NavController.navigateToDetail(article: NewsArticle) {
    val encodedUrl = URLEncoder.encode(article.url, StandardCharsets.UTF_8.toString())
    val encodedImageUrl = URLEncoder.encode(article.urlToImage ?: "", StandardCharsets.UTF_8.toString())
    
    this.navigate("news_detail/${article.title}/${encodedUrl}/${article.description}/${encodedImageUrl}")
}
