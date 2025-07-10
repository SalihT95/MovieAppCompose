package com.turkoglu.moviecomposeapp.presentation.viewall.view

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.turkoglu.moviecomposeapp.domain.model.Movie
import com.turkoglu.moviecomposeapp.presentation.detail.view.CircularBackButtons
import com.turkoglu.moviecomposeapp.presentation.home.MovieListItem
import com.turkoglu.moviecomposeapp.presentation.viewall.ViewAllScreenViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun ViewAllScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ViewAllScreenViewModel = hiltViewModel(),
    navigateToDetail: (Movie) -> Unit
) {
    val movies = viewModel.moviesFlow.value.collectAsLazyPagingItems()
    val title = viewModel.screenTitle.value.movies

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 5.dp, vertical = 5.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(bottom = 8.dp)
            ) {
                CircularBackButtons(onClick = { navController.popBackStack() })
                Text(
                    text = title,
                    fontWeight = FontWeight.Normal,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onBackground

                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(movies.itemSnapshotList.items, key = { it.id ?: it.hashCode() }) { movie ->
                    MovieListItem(
                        modifier = modifier
                            .height(200.dp)
                            .width(160.dp),
                        movie = movie,
                        onMovieClick = { navigateToDetail(movie) }
                    )
                }
            }
        }
    }
}