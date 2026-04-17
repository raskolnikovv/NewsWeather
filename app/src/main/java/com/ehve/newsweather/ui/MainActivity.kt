package com.ehve.newsweather.ui

import android.Manifest
import android.location.Geocoder
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.navigation.NavController
import com.ehve.newsweather.ui.components.BottomNavigationBar
import com.ehve.newsweather.ui.components.WeatherCard
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

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
                Scaffold(
                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        
                        if (currentRoute != null && !currentRoute.contains("news_detail")) {
                            BottomNavigationBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        SetupNavGraph(
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    viewModel: NewsViewModel,
    navController: NavController
) {
    val uiState by viewModel.articles.collectAsState()
    val weatherState by viewModel.weather.collectAsState()
    val isRefreshing = uiState is NewsUiState.Loading
    
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val geocoder = remember { Geocoder(context, Locale.getDefault()) }

    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (locationPermissionState.status.isGranted) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        updateWeather(location, geocoder, viewModel)
                    } else {
                        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                            .addOnSuccessListener { loc ->
                                loc?.let { updateWeather(it, geocoder, viewModel) }
                            }
                    }
                }
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        } else {
            locationPermissionState.launchPermissionRequest()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { 
                viewModel.fetchTopHeadlines()
                locationPermissionState.launchPermissionRequest()
            },
            modifier = Modifier.padding(paddingValues)
        ) {
            when (val state = uiState) {
                is NewsUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is NewsUiState.Success -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            Text(
                                text = "NewsWeather",
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(start = 16.dp, top = 64.dp, bottom = 16.dp)
                            )
                        }

                        weatherState?.let { info ->
                            item {
                                WeatherCard(weather = info)
                            }
                        }

                        item {
                            Text(
                                text = "Principais Notícias",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }

                        items(state.articles) { article ->
                            NewsCard(
                                article = article,
                                onClick = { navController.navigateToDetail(article) }
                            )
                        }
                        
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }

                is NewsUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

/**
 * Helper function to reverse geocode and update weather state.
 */
private fun updateWeather(
    location: android.location.Location,
    geocoder: Geocoder,
    viewModel: NewsViewModel
) {
    val address = try {
        geocoder.getFromLocation(location.latitude, location.longitude, 1)?.firstOrNull()
    } catch (e: Exception) {
        null
    }

    val cityName = address?.locality ?: address?.subAdminArea ?: address?.adminArea ?: "Your Location"
    viewModel.fetchWeather(location.latitude, location.longitude, cityName)
}
