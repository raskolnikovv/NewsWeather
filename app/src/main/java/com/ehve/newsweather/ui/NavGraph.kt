package com.ehve.newsweather.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
                onNewsClick = { article ->
                    val encodedUrl = URLEncoder.encode(article.url, StandardCharsets.UTF_8.toString())
                    navController.navigate("news_detail/${article.title}/${encodedUrl}/${article.description}")
            })
        }

        // Tela de Detalhes
        composable(
            route = "news_detail/{title}/{url}/{desc}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("url") { type = NavType.StringType },
                navArgument("desc") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            NewsDetailScreen(
                title = backStackEntry.arguments?.getString("title"),
                description = backStackEntry.arguments?.getString("desc"),
                url = backStackEntry.arguments?.getString("url"),
                onBack = { navController.popBackStack() }
            )
        }
    }
}