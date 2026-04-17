package com.ehve.newsweather.ui

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

/**
 * Navigation graph definition for the application.
 */
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    viewModel: NewsViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(route = Routes.HOME) {
            HomeScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(
            route = Routes.DETAILS,
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

            val favViewModel: NewsViewModel = hiltViewModel()
            val isFavorite by favViewModel.getIsFavorite(url ?: "").collectAsState(initial = false)

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
                        favViewModel.toggleFavorite(
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

        composable(route = Routes.FAVORITES) {
            FavoritesScreen(
                navController = navController,
                onBack = { navController.popBackStack() },
                onNewsClick = { article -> navController.navigateToDetail(article) }
            )
        }
    }
}
