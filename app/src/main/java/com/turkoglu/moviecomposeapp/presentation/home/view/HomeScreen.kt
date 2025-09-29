package com.turkoglu.moviecomposeapp.presentation.home.view

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.turkoglu.moviecomposeapp.R
import com.turkoglu.moviecomposeapp.domain.model.Genre
import com.turkoglu.moviecomposeapp.domain.model.Movie
import com.turkoglu.moviecomposeapp.domain.model.UserAccount
import com.turkoglu.moviecomposeapp.presentation.Screen
import com.turkoglu.moviecomposeapp.presentation.component.GenreChipsRow
import com.turkoglu.moviecomposeapp.presentation.component.HomeSearchButton
import com.turkoglu.moviecomposeapp.presentation.home.HomeViewModel
import com.turkoglu.moviecomposeapp.presentation.home.MovieListItem
import com.turkoglu.moviecomposeapp.presentation.user.UserViewModel

@SuppressLint("StateFlowValueCalledInComposition")
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
    val currentUser by userViewModel.currentUser.collectAsState()

    Scaffold(
        topBar = { HomeScreenHeader(navController, genreList, currentUser) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .blur(24.dp)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                MovieSection(
                    title = "Popular",
                    type = popularMovies,
                    onClickViewAll = {
                        navController.navigate(Screen.ViewAll.route.replace("{selectedType}", "Popular"))
                    },
                    onMovieClick = navigateToDetail
                )
            }
            item {
                MovieSection(
                    title = "Upcoming",
                    type = upComingMovies,
                    onClickViewAll = {
                        navController.navigate(Screen.ViewAll.route.replace("{selectedType}", "Upcoming"))
                    },
                    onMovieClick = navigateToDetail
                )
            }
            item {
                MovieSection(
                    title = "Top Rated",
                    type = topRatedMovies,
                    onClickViewAll = {
                        navController.navigate(Screen.ViewAll.route.replace("{selectedType}", "Top Rated"))
                    },
                    onMovieClick = navigateToDetail
                )
            }
            item{
                MovieSection(
                    title = "Now Playing",
                    type = nowPlayingMovies,
                    onClickViewAll = {
                        navController.navigate(Screen.ViewAll.route.replace("{selectedType}", "Now Playing"))
                    },
                    onMovieClick = navigateToDetail
                )
            }
        }
    }
}

@Composable
fun HomeTopBar(navController: NavController, account: UserAccount?) {
    val avatarUrl = account?.avatarUrl
    val fullAvatarUrl = avatarUrl?.let { path ->
        if (path.startsWith("/")) "https://image.tmdb.org/t/p/w500$path" else null
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = account?.username ?: "The Movie",
            modifier = Modifier
                .weight(1f),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        HomeSearchButton(onClick = {
            navController.navigate("Search")
        })
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(fullAvatarUrl ?: "https://i.pravatar.cc/150?img=3")
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .crossfade(true)
                .build(),
            contentDescription = "Profile",
            modifier = Modifier
                .size(70.dp)
                .padding(start = 12.dp)
                .clip(MaterialTheme.shapes.large)
        )
    }
}

@Composable
fun HomeScreenHeader(navController: NavController, genreList: List<Genre>, account: UserAccount?) {
    Column(modifier = Modifier.padding(top = 8.dp)) {
        HomeTopBar(navController, account)
        GenreChipsRow(
            textColor = MaterialTheme.colorScheme.onSurface,
            genreList = genreList.map { it.name },
        ) { genre ->
            navController.navigate("ViewAll/$genre")
        }
    }
}

@Composable
fun MovieSection(
    type : LazyPagingItems<Movie>,
    title: String,
    onClickViewAll: () -> Unit,
    onMovieClick: (Movie) -> Unit
) {
    val uniqueIds = mutableSetOf<Int>()
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "View All",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onClickViewAll() }
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(
                type.itemSnapshotList.items,
                key = { index, movie ->
                    val id = movie.id ?: -1
                    if (uniqueIds.contains(id)) {
                        val previousMovie = uniqueIds.find { it == id }
                        uniqueIds.remove(previousMovie)
                    } else {
                        uniqueIds.add(id)
                    }
                    "${movie.id?.toString()}_${index}"
                }
            ) { _, movie ->
                MovieListItem(
                    movie = movie,
                    onItemClick = { onMovieClick(movie) }
                )
            }
        }
    }
}
