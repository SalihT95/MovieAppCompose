package com.turkoglu.moviecomposeapp.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.turkoglu.moviecomposeapp.presentation.ui.MovieComposeAppTheme
import com.turkoglu.moviecomposeapp.presentation.ui.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val themeMode by themeViewModel.theme.collectAsState()
            MovieComposeAppTheme(themeMode = themeMode) {
                val navController = rememberNavController()
                MainScreen(navController)
            }
        }
    }
}
