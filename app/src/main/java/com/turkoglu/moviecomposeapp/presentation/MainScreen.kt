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
        startDestination = if (isUserLoggedIn) Screen.HomeScreen.route else Screen.LoginScreen.route
    ) {
        composable(Screen.LoginScreen.route) {
            AppScaffold(navController, showBottomBar = false) {
                LoginScreen(navController = navController) {
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                    }
                }
            }
        }

        composable(Screen.HomeScreen.route) {
            AppScaffold(navController) {
                HomeScreen(navController = navController) { movie ->
                    navController.navigate("Detail/${movie.id}")
                }
            }
        }

        composable(Screen.SearchScreen.route) {
            AppScaffold(navController) {
                SearchScreen(navController)
            }
        }

        composable(Screen.FavScreen.route) {
            AppScaffold(navController) {
                FavScreen(navController)
            }
        }

        composable(Screen.SettingsScreen.route) {
            AppScaffold(navController) {
                SettingsScreen(navController)
            }
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) {
            AppScaffold(navController) {
                DetailScreen(navController)
            }
        }

        composable(
            route = Screen.ViewAll.route,
            arguments = listOf(navArgument("selectedType") { type = NavType.StringType })
        ) {
            AppScaffold(navController) {
                ViewAllScreen(navController) { movie ->
                    navController.navigate("Detail/${movie.id}")
                }
            }
        }
    }
}
