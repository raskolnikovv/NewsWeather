package com.ehve.newsweather.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ehve.newsweather.domain.model.NewsArticle
import com.ehve.newsweather.ui.favorites.FavoritesScreen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    viewModel: NewsViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // Tela Principal
        composable(route = "home") {
            HomeScreen(
                viewModel = viewModel,
                navController = navController,
                onNewsClick = { article ->
                    val encodedUrl = URLEncoder.encode(article.url, StandardCharsets.UTF_8.toString())
                    val encodedImageUrl = URLEncoder.encode(article.urlToImage ?: "", StandardCharsets.UTF_8.toString())
                    navController.navigate("news_detail/${article.title}/$encodedUrl/${article.description}/$encodedImageUrl")
            })
        }

        // Tela de Detalhes
        composable(
            route = "news_detail/{title}/{url}/{desc}/{imageUrl}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("url") { type = NavType.StringType },
                navArgument("desc") { type = NavType.StringType },
                navArgument("imageUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title")
            val url = backStackEntry.arguments?.getString("url")
            val desc = backStackEntry.arguments?.getString("desc")
            val imageUrl = backStackEntry.arguments?.getString("imageUrl")
            val context = androidx.compose.ui.platform.LocalContext.current
            val customTabsIntent = androidx.browser.customtabs.CustomTabsIntent.Builder().build()

            val viewModel: NewsViewModel = hiltViewModel()
            val isFavorite by viewModel.getIsFavorite(url ?: "").collectAsState(initial = false)


            NewsDetailScreen(
                title = title,
                description = desc,
                urlToImage = imageUrl,
                url = url,
                onBack = { navController.popBackStack() },
                onUrlClick = { customTabsIntent.launchUrl(context, android.net.Uri.parse(it)) },
                isFavorite = isFavorite,
                onFavoriteClick = {
                    if (url != null && title != null && desc != null) {
                        viewModel.toggleFavorite(
                            NewsArticle(
                                title = title,
                                description = desc,
                                urlToImage = imageUrl ?: "",
                                url = url
                            )
                        )
                    }
                }
            )
        }
        composable("favorites") {
            FavoritesScreen(
                navController = navController,
                onBack = { navController.popBackStack() },
                onNewsClick = { article ->
                    val encodedUrl = URLEncoder.encode(article.url, StandardCharsets.UTF_8.toString())
                    val encodedImage = URLEncoder.encode(article.urlToImage ?: "", StandardCharsets.UTF_8.toString())

                    navController.navigate("news_detail/${article.title}/$encodedUrl/${article.description}/$encodedImage")
                }
            )
        }
    }
}