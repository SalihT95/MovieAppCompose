package com.turkoglu.composedeneme.presentation

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.turkoglu.composedeneme.presentation.detail.view.DetailScreen
import com.turkoglu.composedeneme.presentation.fav.view.FavScreen
import com.turkoglu.composedeneme.presentation.home.view.HomeScreen
import com.turkoglu.composedeneme.presentation.login.LoginViewModel
import com.turkoglu.composedeneme.presentation.login.views.LoginScreen
import com.turkoglu.composedeneme.presentation.search.views.SearchScreen
import com.turkoglu.composedeneme.presentation.settings.view.SettingsScreen
import com.turkoglu.composedeneme.presentation.viewall.view.ViewAllScreen

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun MainScreen(
    navController: NavHostController
) {
    val viewModel: LoginViewModel = hiltViewModel()
    val isUserLoggedIn = viewModel.getRememberMeStatus()

    NavHost(
        navController = navController,
        startDestination =if (isUserLoggedIn) {
            Screen.HomeScreen.route
        } else {
            Screen.LoginScreen.route
        }
    ) {
        composable(Screen.LoginScreen.route) {
            Scaffold (modifier = Modifier.background(MaterialTheme.colors.background)) { innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(
                            PaddingValues(
                                0.dp,
                                0.dp,
                                0.dp,
                                innerPadding.calculateBottomPadding()
                            )
                        )
                        .background(MaterialTheme.colors.background)

                ) {
                    LoginScreen(navController = navController){
                        navController.navigate(Screen.HomeScreen.route)
                    }
                }
            }
        }
        composable(Screen.HomeScreen.route) {
            Scaffold(
                modifier = Modifier.background(MaterialTheme.colors.background),
                bottomBar = {
                    Modifier.background(MaterialTheme.colors.background)
                    BottomAppBar(modifier = Modifier.background(MaterialTheme.colors.background)) {
                        BottomNavigationBar(navController = navController)

                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(
                            PaddingValues(
                                0.dp,
                                0.dp,
                                0.dp,
                                innerPadding.calculateBottomPadding()
                            )
                        )
                        .background(MaterialTheme.colors.background)

                ) {
                    //Navigations(navController = navController)
                    HomeScreen(navController = navController){
                        navController.navigate(
                            "Detail/${it.id}"
                        )
                    }

                }
            }
        }
        composable(Screen.SearchScreen.route) {
            Scaffold(
                modifier = Modifier.background(MaterialTheme.colors.background),
                bottomBar = {
                    Modifier.background(MaterialTheme.colors.background)
                    BottomAppBar(modifier = Modifier.background(MaterialTheme.colors.background)) {
                        BottomNavigationBar(navController = navController)

                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(
                            PaddingValues(
                                0.dp,
                                0.dp,
                                0.dp,
                                innerPadding.calculateBottomPadding()
                            )
                        )
                        .background(MaterialTheme.colors.background)

                ) {
                    //Navigations(navController = navController)
                    SearchScreen(navController)

                }
            }



        }
        composable(
            Screen.FavScreen.route
        ) {

            Scaffold(
                modifier = Modifier.background(MaterialTheme.colors.background),
                bottomBar = {
                    Modifier.background(MaterialTheme.colors.background)
                    BottomAppBar(modifier = Modifier.background(MaterialTheme.colors.background)) {
                        BottomNavigationBar(navController = navController)

                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(
                            PaddingValues(
                                0.dp,
                                0.dp,
                                0.dp,
                                innerPadding.calculateBottomPadding()
                            )
                        )
                        .background(MaterialTheme.colors.background)

                ) {
                    //Navigations(navController = navController)
                    FavScreen(navController)

                }
            }



        }
        composable(Screen.SettingsScreen.route) {

            Scaffold(
                modifier = Modifier.background(MaterialTheme.colors.background),
                bottomBar = {
                    Modifier.background(MaterialTheme.colors.background)
                    BottomAppBar(modifier = Modifier.background(MaterialTheme.colors.background)) {
                        BottomNavigationBar(navController = navController)

                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(
                            PaddingValues(
                                0.dp,
                                0.dp,
                                0.dp,
                                innerPadding.calculateBottomPadding()
                            )
                        )
                        .background(MaterialTheme.colors.background)

                ) {
                    //Navigations(navController = navController)
                    SettingsScreen(navController)

                }
            }

        }
        composable(
            Screen.Detail.route,
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) {

            Scaffold(
                modifier = Modifier.background(MaterialTheme.colors.background),
                bottomBar = {
                    Modifier.background(MaterialTheme.colors.background)
                    BottomAppBar(modifier = Modifier.background(MaterialTheme.colors.background)) {
                        BottomNavigationBar(navController = navController)

                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(
                            PaddingValues(
                                0.dp,
                                0.dp,
                                0.dp,
                                innerPadding.calculateBottomPadding()
                            )
                        )
                        .background(MaterialTheme.colors.background)

                ) {
                    DetailScreen(navController)

                }
            }

        }

        composable(
            Screen.ViewAll.route,
            arguments = listOf(navArgument("selectedType") { type = NavType.StringType })
        ) {

            Scaffold(
                modifier = Modifier.background(MaterialTheme.colors.background),
                bottomBar = {
                    Modifier.background(MaterialTheme.colors.background)
                    BottomAppBar(modifier = Modifier.background(MaterialTheme.colors.background)) {
                        BottomNavigationBar(navController = navController)

                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(
                            PaddingValues(
                                0.dp,
                                0.dp,
                                0.dp,
                                innerPadding.calculateBottomPadding()
                            )
                        )
                        .background(MaterialTheme.colors.background)

                ) {
                    ViewAllScreen(navController){
                        navController.navigate(
                            "Detail/${it.id}"
                        )
                    }

                }
            }

        }



    }

/*
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colors.background),
        bottomBar = {
            BottomAppBar(modifier = Modifier.background(MaterialTheme.colors.background)) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(
                    PaddingValues(
                        0.dp,
                        0.dp,
                        0.dp,
                        innerPadding.calculateBottomPadding()
                    )
                )
                .background(MaterialTheme.colors.background)
        ) {
            Navigations(navController = navController)
        }
    }*/
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun Navigations(navController: NavHostController) {
    NavHost(navController= navController, startDestination = Screen.HomeScreen.route) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController, navigateToDetail = {movie ->
                navController.navigate("Detail/${movie.id}") })
        }
        composable(Screen.SearchScreen.route) {
            SearchScreen(navController)
        }
        composable(
            Screen.FavScreen.route
        ) {
            FavScreen(navController)
        }
        composable(Screen.SettingsScreen.route) {
            SettingsScreen(navController)
        }
        composable(
            Screen.Detail.route,
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) {
            DetailScreen(navController)
        }

        composable(
            Screen.ViewAll.route,
            arguments = listOf(navArgument("selectedType") { type = NavType.StringType })
        ) {
            ViewAllScreen(navController){
                navController.navigate(
                    "Detail/${it.id}"
                )
            }
        }

    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Screen.HomeScreen,
        Screen.SearchScreen,
        Screen.FavScreen,
        Screen.SettingsScreen,
    )
    var selectedItem by remember { mutableIntStateOf(0) }
    var currentRoute by remember { mutableStateOf(Screen.HomeScreen.route) }

    items.forEachIndexed { index, Screen ->
        if (Screen.route == currentRoute) {
            selectedItem = index
        }
    }

    NavigationBar (modifier = Modifier.background(MaterialTheme.colors.background)){
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                alwaysShowLabel = true,
                icon = { Icon(item.icon!!, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    currentRoute = item.route
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}