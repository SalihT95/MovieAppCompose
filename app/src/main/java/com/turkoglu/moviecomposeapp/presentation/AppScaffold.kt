package com.turkoglu.moviecomposeapp.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.turkoglu.moviecomposeapp.presentation.component.BottomNavigationBar

@Composable
fun AppScaffold(
    navController: NavHostController,
    showBottomBar: Boolean = true,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    BottomNavigationBar(navController = navController)
                }
            }
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
            ) {
                content(padding)
            }
        }
    )
}
