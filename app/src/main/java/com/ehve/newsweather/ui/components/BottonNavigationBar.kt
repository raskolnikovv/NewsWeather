package com.ehve.newsweather.ui.components

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

@Composable
fun BottomNavigationBar(navController: NavController) {
    Surface(
        modifier = Modifier
            .padding(24.dp)
            .clip(RoundedCornerShape(32.dp)),
        color = Color.White,
        tonalElevation = 8.dp,
        shadowElevation = 8.dp
    ){
        NavigationBar(
            containerColor = Color.White,
            contentColor = Color.Black,
            tonalElevation = 0.dp
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            NavigationBarItem(
                selected = currentRoute == "home",
                onClick = { navController.navigate("home") },
                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    indicatorColor = Color(0xFFF0F0F0),
                )
            )
            NavigationBarItem(
                selected = currentRoute == "favorites",
                onClick = { navController.navigate("favorites") },
                icon = { Icon(Icons.Default.BookmarkBorder, contentDescription = null) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    indicatorColor = Color(0xFFF0F0F0),
                )
            )
        }
    }
}