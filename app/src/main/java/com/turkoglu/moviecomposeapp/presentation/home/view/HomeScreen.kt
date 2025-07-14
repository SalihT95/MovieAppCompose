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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.turkoglu.moviecomposeapp.R
import com.turkoglu.moviecomposeapp.domain.model.Movie
import com.turkoglu.moviecomposeapp.presentation.Screen
import com.turkoglu.moviecomposeapp.presentation.component.GenreChipsRow
import com.turkoglu.moviecomposeapp.presentation.component.HomeSearchButton
import com.turkoglu.moviecomposeapp.presentation.home.HomeViewModel
import com.turkoglu.moviecomposeapp.presentation.home.MovieListItem

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToDetail: (Movie) -> Unit
) {
    val popularMovies = viewModel.popularState.value.collectAsLazyPagingItems()
    val topRatedMovies = viewModel.topRatedState.value.collectAsLazyPagingItems()
    val nowPlayingMovies = viewModel.nowPlayingState.value.collectAsLazyPagingItems()
    val upComingMovies = viewModel.upComingState.value.collectAsLazyPagingItems()

    Scaffold(
        topBar = { HomeScreenHeader(navController) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
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
fun HomeTopBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Movie",
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        HomeSearchButton(onClick = {
            navController.navigate("Search")
        })
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://i.pravatar.cc/150?img=3")
                .placeholder(R.drawable.ic_placeholder) // Yüklenene kadar gösterilecek
                .error(R.drawable.ic_placeholder)       // Hata olursa gösterilecek
                .crossfade(true)
                .build(),
            contentDescription = "Profile",
            modifier = Modifier
                .size(70.dp)
                .padding(start = 16.dp)
                .clip(MaterialTheme.shapes.large)
        )
    }
}



@Composable
fun HomeScreenHeader(navController: NavController) {
    Column {
        HomeTopBar(navController)
        Spacer(modifier = Modifier.height(8.dp))
        GenreChipsRow (textColor = Color.Black, genreList = null){ genre ->
            navController.navigate("ViewAll/$genre")
        }
    }
}

@Composable
fun FeaturedMovieSection(movie: Movie, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .clip(MaterialTheme.shapes.large)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movie.posterPath)
                .placeholder(R.drawable.ic_placeholder) // Yüklenene kadar gösterilecek
                .error(R.drawable.ic_placeholder)       // Hata olursa gösterilecek
                .crossfade(true)
                .build(),
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(text = movie.title, color = Color.White, style = MaterialTheme.typography.titleLarge)
            Text(
                text = movie.description.take(60) + "...",
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun HorizontalMovieList(movies: List<Movie>, onMovieClick: (Movie) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(movies) { movie ->
            MovieListItem(movie = movie, onItemClick = { onMovieClick(movie) })
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
            horizontalArrangement = Arrangement.SpaceBetween
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LazyRow(content = {
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
                        val key = "${movie.id?.toString()}_${index}"
                        key
                    }
                ) { _, movie ->
                    MovieListItem(
                        movie = movie,
                        onItemClick = {onMovieClick(movie)}
                    )
                }
            })
        }
    }
}
