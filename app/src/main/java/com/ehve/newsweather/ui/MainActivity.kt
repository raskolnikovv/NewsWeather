package com.ehve.newsweather.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ehve.newsweather.ui.components.BottomNavigationBar

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            val navController = rememberNavController()

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                SetupNavGraph(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: NewsViewModel,
    navController: NavController,
    onNewsClick: (com.ehve.newsweather.domain.model.NewsArticle) -> Unit
) {
    val uiState by viewModel.articles.collectAsState()

    val isRefreshing = uiState is NewsUiState.Loading

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "NewsWeather",
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.fetchTopHeadlines() },
            modifier = Modifier.padding(paddingValues)
        ) {
            when (val state = uiState) {
                is NewsUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is NewsUiState.Success -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.articles) { article ->
                            NewsCard(
                                article = article,
                                onClick = { onNewsClick(article) }
                            )
                        }
                    }
                }

                is NewsUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}
//@Composable
//fun BottomNavigationBar(navController: NavController) {
//    Surface(
//        modifier = Modifier
//            .padding(24.dp)
//            .clip(RoundedCornerShape(32.dp)),
//        color = Color.White,
//        tonalElevation = 8.dp,
//        shadowElevation = 8.dp
//    ){
//        NavigationBar(
//            containerColor = Color.White,
//            contentColor = Color.Black,
//            tonalElevation = 0.dp
//        ) {
//            val navBackStackEntry by navController.currentBackStackEntryAsState()
//            val currentRoute = navBackStackEntry?.destination?.route
//
//            NavigationBarItem(
//                selected = currentRoute == "home",
//                onClick = { navController.navigate("home") },
//                icon = { Icon(Icons.Default.Home, contentDescription = null) },
//                colors = NavigationBarItemDefaults.colors(
//                    selectedIconColor = Color.Black,
//                    indicatorColor = Color(0xFFF0F0F0),
//                )
//            )
//            NavigationBarItem(
//                selected = currentRoute == "favorites",
//                onClick = { navController.navigate("favorites") },
//                icon = { Icon(Icons.Default.BookmarkBorder, contentDescription = null) },
//                colors = NavigationBarItemDefaults.colors(
//                    selectedIconColor = Color.Black,
//                    indicatorColor = Color(0xFFF0F0F0),
//                )
//            )
//        }
//    }
//}




