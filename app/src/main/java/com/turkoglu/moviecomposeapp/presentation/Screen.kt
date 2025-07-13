package com.turkoglu.moviecomposeapp.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val icon: ImageVector? = null,
    val title: String = "",
    val isBottomBarVisible: Boolean = false
) {
    val baseRoute: String
        get() = route.substringBefore("/{")

    object Login : Screen("Login", Icons.Rounded.AccountCircle, "Login", false)
    object Home : Screen("Home", Icons.Rounded.Home, "Home", true)
    object Fav : Screen("Fav", Icons.Rounded.Favorite, "Favorites", true)
    object Settings : Screen("Settings", Icons.Rounded.Settings, "Settings", true)
    object Search : Screen("Search", Icons.Rounded.Search, "Search", false)
    object Detail : Screen("Detail/{movieId}", Icons.Rounded.Info, "Detail", false)
    object ViewAll : Screen("ViewAll/{selectedType}", Icons.Rounded.Info, "View All", false)
}
