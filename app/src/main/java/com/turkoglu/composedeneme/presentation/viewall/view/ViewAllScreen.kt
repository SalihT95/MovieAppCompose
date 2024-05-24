package com.turkoglu.composedeneme.presentation.viewall.view

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.turkoglu.composedeneme.domain.model.Movie
import com.turkoglu.composedeneme.presentation.detail.view.CircularBackButtons
import com.turkoglu.composedeneme.presentation.home.MovieListItem
import com.turkoglu.composedeneme.presentation.viewall.ViewAllScreenViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun ViewAllScreen (
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ViewAllScreenViewModel = hiltViewModel(),
    navigateToDetail: (Movie) -> Unit
){
    val warMovie = viewModel.warState.value.collectAsLazyPagingItems()
    val nameState = viewModel.nameState.value.movies
    val uniqueIds = mutableSetOf<Int>()
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 10.dp)
        ) {
            CircularBackButtons(
                onClick = { navController.popBackStack() })

            Text(text = nameState, color = Color.White, fontSize = 28.sp)
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(16.dp)
        ) {
            itemsIndexed(
                warMovie.itemSnapshotList.items,
                key = { index, movie ->
                    val id = movie.id ?: -1
                    if (uniqueIds.contains(id)) {
                        val previousMovie = uniqueIds.find { it == id }
                        uniqueIds.remove(previousMovie)
                    } else {
                        uniqueIds.add(id)
                    }
                    val key = "${movie.id?.toString()}_${index}"
                    Log.d("LazyRowKey", "Key: $key, Index: $index, Movie ID: ${movie.id}")
                    key
                }
            ) { _, movie ->
                MovieListItem(
                    modifier = modifier
                        .height(200.dp)
                        .width(180.dp),
                    movie = movie,
                    onMovieClick = {navigateToDetail(movie)}
                )
            }
        }
    }
}