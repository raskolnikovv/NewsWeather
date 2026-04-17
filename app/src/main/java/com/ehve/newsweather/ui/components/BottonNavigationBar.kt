package com.ehve.newsweather.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * Custom floating Bottom Navigation Bar.
 */
@Composable
fun BottomNavigationBar(navController: NavController) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 48.dp, vertical = 32.dp)
            .height(84.dp)
            .clip(RoundedCornerShape(32.dp)),
        color = Color.White.copy(alpha = 0.95f),
        tonalElevation = 8.dp,
        shadowElevation = 12.dp
    ){
        NavigationBar(
            containerColor = Color.Transparent,
            contentColor = Color.Black,
            tonalElevation = 0.dp
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            NavigationBarItem(
                selected = currentRoute == "home",
                onClick = {
                    if (currentRoute != "home") navController.navigate("home")
                },
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    indicatorColor = Color(0xFFF0F0F0),
                    unselectedIconColor = Color.Gray
                )
            )
            NavigationBarItem(
                selected = currentRoute == "favorites",
                onClick = {
                    if (currentRoute != "favorites") navController.navigate("favorites")
                },
                icon = { Icon(Icons.Default.BookmarkBorder, contentDescription = "Favorites") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    indicatorColor = Color(0xFFF0F0F0),
                    unselectedIconColor = Color.Gray
                )
            )
        }
    }
}
