package com.turkoglu.moviecomposeapp.presentation

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.turkoglu.moviecomposeapp.presentation.detail.view.DetailScreen
import com.turkoglu.moviecomposeapp.presentation.fav.view.FavScreen
import com.turkoglu.moviecomposeapp.presentation.home.view.HomeScreen
import com.turkoglu.moviecomposeapp.presentation.login.LoginViewModel
import com.turkoglu.moviecomposeapp.presentation.login.views.LoginScreen
import com.turkoglu.moviecomposeapp.presentation.search.views.SearchScreen
import com.turkoglu.moviecomposeapp.presentation.settings.view.SettingsScreen
import com.turkoglu.moviecomposeapp.presentation.viewall.view.ViewAllScreen

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun MainScreen(navController: NavHostController) {
    val viewModel: LoginViewModel = hiltViewModel()
    val isUserLoggedIn = viewModel.getRememberMeStatus()

    NavHost(
        navController = navController,
        startDestination = if (isUserLoggedIn) Screen.Home.route else Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            AppScaffold(navController, showBottomBar = Screen.Login.isBottomBarVisible) {
                LoginScreen(navController = navController) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }
        }

        composable(Screen.Home.route) {
            AppScaffold(navController, showBottomBar = Screen.Home.isBottomBarVisible) {
                HomeScreen(navController) { movie ->
                    navController.navigate(Screen.Detail.route.replace("{movieId}", movie.id.toString()))
                }
            }
        }

        composable(Screen.Fav.route) {
            AppScaffold(navController, showBottomBar = Screen.Fav.isBottomBarVisible) {
                FavScreen(navController)
            }
        }

        composable(Screen.Settings.route) {
            AppScaffold(navController, showBottomBar = Screen.Settings.isBottomBarVisible) {
                SettingsScreen(navController)
            }
        }

        composable(Screen.Search.route) {
            AppScaffold(navController, showBottomBar = Screen.Search.isBottomBarVisible) {
                SearchScreen(navController)
            }
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) {
            AppScaffold(navController, showBottomBar = Screen.Detail.isBottomBarVisible) {
                DetailScreen(navController)
            }
        }

        composable(
            route = Screen.ViewAll.route,
            arguments = listOf(navArgument("selectedType") { type = NavType.StringType })
        ) {
            AppScaffold(navController, showBottomBar = Screen.ViewAll.isBottomBarVisible) {
                ViewAllScreen(navController) { movie ->
                    navController.navigate(Screen.Detail.route.replace("{movieId}", movie.id.toString()))
                }
            }
        }
    }
}
