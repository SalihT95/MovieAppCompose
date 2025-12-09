package com.turkoglu.moviecomposeapp.presentation

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.turkoglu.moviecomposeapp.presentation.cast.view.CastScreen
import com.turkoglu.moviecomposeapp.presentation.detail.view.DetailScreen
import com.turkoglu.moviecomposeapp.presentation.fav.view.FavScreen
import com.turkoglu.moviecomposeapp.presentation.home.view.HomeScreen
import com.turkoglu.moviecomposeapp.presentation.login.AuthViewModel
import com.turkoglu.moviecomposeapp.presentation.login.views.LoginScreen
import com.turkoglu.moviecomposeapp.presentation.login.views.RegisterScreen // Yeni Import
import com.turkoglu.moviecomposeapp.presentation.onboarding.OnboardingScreen
import com.turkoglu.moviecomposeapp.presentation.search.views.SearchScreen
import com.turkoglu.moviecomposeapp.presentation.settings.view.AboutScreen
import com.turkoglu.moviecomposeapp.presentation.settings.view.SettingsScreen
import com.turkoglu.moviecomposeapp.presentation.splash.SplashScreen
import com.turkoglu.moviecomposeapp.presentation.ui.ThemeViewModel
import com.turkoglu.moviecomposeapp.presentation.viewall.view.ViewAllScreen

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun MainScreen(
    navController: NavHostController,
    themeViewModel: ThemeViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val onboardingDone by themeViewModel.onboardingDone.collectAsStateWithLifecycle()
    // Not: AuthViewModel'de rememberMe mantığını Firebase'e göre güncellemiştik.
    // Eğer sadece "Beni Hatırla" seçiliyse direkt Home'a atar.
    val isRemembered by authViewModel.rememberMe.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // --- SPLASH SCREEN ---
        composable(Screen.Splash.route) {
            AppScaffold(navController, showBottomBar = false) {
                SplashScreen {
                    when {
                        !onboardingDone -> {
                            navController.navigate(Screen.Onboarding.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true }
                            }
                        }
                        // Eğer beni hatırla aktifse VE Firebase'de oturum açıksa (ViewModel init bloğunda kontrol ediliyor)
                        isRemembered -> {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true }
                            }
                        }

                        else -> {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true }
                            }
                        }
                    }
                }
            }
        }

        // --- ONBOARDING SCREEN ---
        composable(Screen.Onboarding.route) {
            AppScaffold(navController, showBottomBar = false) {
                OnboardingScreen({
                    themeViewModel.setOnboardingDone()
                    navController.navigate(if (isRemembered) Screen.Home.route else Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                })
            }
        }

        // --- LOGIN SCREEN ---
        composable(Screen.Login.route) {
            AppScaffold(navController, showBottomBar = Screen.Login.isBottomBarVisible) {
                LoginScreen(
                    navController = navController,
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    // Yeni eklediğimiz callback: Kayıt ekranına git
                    onNavigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    }
                )
            }
        }

        // --- REGISTER SCREEN (YENİ EKLENDİ) ---
        composable(Screen.Register.route) {
            // Kayıt ekranında BottomBar göstermiyoruz
            AppScaffold(navController, showBottomBar = false) {
                RegisterScreen(
                    navController = navController,
                    onRegisterSuccess = {
                        // Başarılı kayıttan sonra Home ekranına at ve geri tuşuna basınca Login'e dönmesin
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        // --- HOME SCREEN ---
        composable(Screen.Home.route) {
            AppScaffold(navController, showBottomBar = Screen.Home.isBottomBarVisible) {
                HomeScreen(navController) { movie ->
                    navController.navigate(
                        Screen.Detail.route.replace(
                            "{movieId}",
                            movie.id.toString()
                        )
                    )
                }
            }
        }

        // --- FAV SCREEN ---
        composable(Screen.Fav.route) {
            AppScaffold(navController, showBottomBar = Screen.Fav.isBottomBarVisible) {
                FavScreen(navController)
            }
        }

        // --- SETTINGS SCREEN ---
        composable(Screen.Settings.route) {
            AppScaffold(navController, showBottomBar = Screen.Settings.isBottomBarVisible) {
                SettingsScreen(navController)
            }
        }

        // --- SEARCH SCREEN ---
        composable(Screen.Search.route) {
            AppScaffold(navController, showBottomBar = Screen.Search.isBottomBarVisible) {
                SearchScreen(navController)
            }
        }

        // --- DETAIL SCREEN ---
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) {
            AppScaffold(navController, showBottomBar = Screen.Detail.isBottomBarVisible) {
                DetailScreen(navController)
            }
        }

        // --- VIEW ALL SCREEN ---
        composable(
            route = Screen.ViewAll.route,
            arguments = listOf(navArgument("selectedType") { type = NavType.StringType })
        ) {
            AppScaffold(navController, showBottomBar = Screen.ViewAll.isBottomBarVisible) {
                ViewAllScreen(navController) { movie ->
                    navController.navigate(
                        Screen.Detail.route.replace(
                            "{movieId}",
                            movie.id.toString()
                        )
                    )
                }
            }
        }

        // --- CAST SCREEN ---
        composable(
            route = Screen.Cast.route,
            arguments = listOf(navArgument("personId") { type = NavType.IntType })
        ) {
            AppScaffold(navController, showBottomBar = false) {
                CastScreen(navController)
            }
        }

        // --- ABOUT SCREEN ---
        composable("About") {
            AppScaffold(navController, showBottomBar = false) {
                AboutScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}