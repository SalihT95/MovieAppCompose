package com.turkoglu.moviecomposeapp.presentation.home.view

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.turkoglu.moviecomposeapp.domain.model.Genre
import com.turkoglu.moviecomposeapp.domain.model.Movie
import com.turkoglu.moviecomposeapp.domain.model.UserAccount
import com.turkoglu.moviecomposeapp.presentation.Screen
import com.turkoglu.moviecomposeapp.presentation.component.GenreChipsRow
import com.turkoglu.moviecomposeapp.presentation.component.HomeSearchButton
import com.turkoglu.moviecomposeapp.presentation.component.ProfileImage
import com.turkoglu.moviecomposeapp.presentation.home.HomeViewModel
import com.turkoglu.moviecomposeapp.presentation.home.MovieListItem
import com.turkoglu.moviecomposeapp.presentation.ui.AppBackgroundGradient
import com.turkoglu.moviecomposeapp.presentation.user.UserViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    navigateToDetail: (Movie) -> Unit
) {
    val popularMovies = viewModel.popularState.value.collectAsLazyPagingItems()
    val topRatedMovies = viewModel.topRatedState.value.collectAsLazyPagingItems()
    val nowPlayingMovies = viewModel.nowPlayingState.value.collectAsLazyPagingItems()
    val upComingMovies = viewModel.upComingState.value.collectAsLazyPagingItems()
    val genreList = viewModel.genres.value.collectAsState(initial = emptyList()).value

    // UserViewModel'den güncel kullanıcıyı dinliyoruz
    val currentUser by userViewModel.currentUser.collectAsState()

    Scaffold(
        topBar = { HomeScreenHeader(navController, genreList, currentUser) },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.background(AppBackgroundGradient)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackgroundGradient)
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            item {
                MovieSection(
                    title = "Popular",
                    type = popularMovies,
                    onClickViewAll = {
                        navController.navigate(
                            Screen.ViewAll.route.replace("{selectedType}", "Popular")
                        )
                    },
                    onMovieClick = navigateToDetail
                )
            }
            item {
                MovieSection(
                    title = "Now Playing",
                    type = nowPlayingMovies,
                    onClickViewAll = {
                        navController.navigate(
                            Screen.ViewAll.route.replace("{selectedType}", "Now Playing")
                        )
                    },
                    onMovieClick = navigateToDetail
                )
            }
            item {
                MovieSection(
                    title = "Upcoming",
                    type = upComingMovies,
                    onClickViewAll = {
                        navController.navigate(
                            Screen.ViewAll.route.replace("{selectedType}", "Upcoming")
                        )
                    },
                    onMovieClick = navigateToDetail
                )
            }
            item {
                MovieSection(
                    title = "Top Rated",
                    type = topRatedMovies,
                    onClickViewAll = {
                        navController.navigate(
                            Screen.ViewAll.route.replace("{selectedType}", "Top Rated")
                        )
                    },
                    onMovieClick = navigateToDetail
                )
            }
        }
    }
}

@Composable
fun MovieSection(
    type: LazyPagingItems<Movie>,
    title: String,
    onClickViewAll: () -> Unit,
    onMovieClick: (Movie) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            AssistChip(
                onClick = { onClickViewAll() },
                label = {
                    Text(
                        text = "View All", color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(type.itemCount, key = { index -> type[index]?.id ?: index }) { index ->
                type[index]?.let { movie ->
                    MovieListItem(movie = movie, onItemClick = { onMovieClick(movie) })
                }
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
fun HomeTopBar(navController: NavController, account: UserAccount?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = account?.username ?: "Misafir",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1
            )
        }

        HomeSearchButton(onClick = {
            navController.navigate("Search")
        })

        ProfileImage(
            avatarKey = account?.avatarUrl,
            modifier = Modifier
                .size(50.dp)
                .padding(start = 12.dp)
                .clip(MaterialTheme.shapes.large)
                .clickable {
                    navController.navigate(Screen.Settings.route)
                }
        )
    }
}

@Composable
fun HomeScreenHeader(navController: NavController, genreList: List<Genre>, account: UserAccount?) {
    Column(modifier = Modifier.padding(top = 8.dp)) {
        HomeTopBar(navController, account)
        Box(modifier = Modifier.padding(start = 10.dp)) {
            GenreChipsRow(
                textColor = MaterialTheme.colorScheme.onSurface,
                genreList = genreList.map { it.name },
            ) { genre ->
                navController.navigate("ViewAll/$genre")
            }
        }
    }
}