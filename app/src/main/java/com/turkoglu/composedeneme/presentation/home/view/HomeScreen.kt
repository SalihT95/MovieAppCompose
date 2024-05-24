package com.turkoglu.composedeneme.presentation.home.view

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.turkoglu.composedeneme.domain.model.Movie
import com.turkoglu.composedeneme.presentation.home.HomeViewModel
import com.turkoglu.composedeneme.presentation.home.MovieListItem

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToDetail: (Movie) -> Unit
) {
    val popularMovies = viewModel.popularState.value.collectAsLazyPagingItems()
    val topratedMovies = viewModel.topretedState.value.collectAsLazyPagingItems()
    val nowPlayingMovies = viewModel.notPlayingState.value.collectAsLazyPagingItems()
    val upCommingMovies = viewModel.upCommingState.value.collectAsLazyPagingItems()
    val actionMovies = viewModel.actionState.value.collectAsLazyPagingItems()
    val animationMovies = viewModel.animationState.value.collectAsLazyPagingItems()
    val comedyMovies = viewModel.comedystate.value.collectAsLazyPagingItems()
    val dramaMovies = viewModel.dramaState.value.collectAsLazyPagingItems()
    val fantasyMovies = viewModel.fantasyState.value.collectAsLazyPagingItems()
    val historyMovies = viewModel.historyState.value.collectAsLazyPagingItems()
    val warMovie = viewModel.warState.value.collectAsLazyPagingItems()
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn {
            //-------------------TopRated-----------------------
            item(content = { CustomText("Top Rated" ,modifier , navController) })
            item(content = { CustomMovies(type = topratedMovies, modifier = modifier, navigateToDetail = navigateToDetail) })
            // ----------------Popular-----------------------
            item { CustomText(name = "Popular", modifier =modifier , navController =navController ) }
            item(content = { CustomMovies(type = popularMovies, modifier = modifier, navigateToDetail = navigateToDetail) })
            // --------------NowPlaying----------------------
            item { CustomText(name = "Now Playing", modifier = modifier, navController = navController ) }
            item(content = { CustomMovies(type = nowPlayingMovies, modifier = modifier, navigateToDetail = navigateToDetail) })
            //------------Upcoming-----------------
            item { CustomText(name = "Upcoming", modifier =modifier , navController =navController ) }
            item(content = { CustomMovies(type = upCommingMovies, modifier = modifier, navigateToDetail = navigateToDetail) })
            //---Action
            item { CustomText(name = "Action", modifier = modifier, navController =navController ) }
            item(content = { CustomMovies(type = actionMovies, modifier = modifier, navigateToDetail = navigateToDetail) })
            //---Animation
            item { CustomText(name = "Animation", modifier = modifier , navController = navController) }
            item(content = { CustomMovies(type = animationMovies, modifier = modifier, navigateToDetail = navigateToDetail) })
            //---Comedy
            item { CustomText(name = "Comedy", modifier = modifier, navController = navController) }
            item(content = { CustomMovies(type = comedyMovies, modifier = modifier, navigateToDetail = navigateToDetail) })
            //---Drama
            item { CustomText(name = "Drama", modifier = modifier, navController = navController) }
            item(content = { CustomMovies(type = dramaMovies, modifier = modifier, navigateToDetail = navigateToDetail) })
            //---Fantasy
            item { CustomText(name = "Fantasy", modifier = modifier, navController = navController) }
            item(content = { CustomMovies(type = fantasyMovies, modifier = modifier, navigateToDetail = navigateToDetail) })
            //---History
            item { CustomText(name = "History", modifier = modifier, navController = navController) }
            item(content = { CustomMovies(type = historyMovies, modifier = modifier, navigateToDetail = navigateToDetail) })
            //---War
            item { CustomText(name = "War", modifier = modifier, navController = navController) }
            item(content = { CustomMovies(type = warMovie, modifier = modifier, navigateToDetail = navigateToDetail) })
        }
    }
}
@Composable
fun CustomText(name : String ,modifier: Modifier , navController: NavController) {
    Spacer(modifier = modifier.height(10.dp))
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp), // Sayfa kenarlarından padding
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = name, color = Color.White, fontSize = 18.sp)

        Text(
            text = "View All",
            color = Color.White,
            fontSize = 18.sp,
            modifier = modifier.clickable {
                navController.navigate("ViewAll/${name}")
            })
    }
}

@Composable
fun CustomMovies(type : LazyPagingItems<Movie> , modifier: Modifier ,navigateToDetail: (Movie) -> Unit) {
    val uniqueIds = mutableSetOf<Int>()
    Box(
        modifier
            .fillMaxWidth()
            .height(220.dp),
        contentAlignment = Alignment.Center
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
                    //Log.d("LazyRowKey", "Key: $key, Index: $index, Movie ID: ${movie.id}")
                    key
                }
            ) { _, movie ->
                MovieListItem(modifier = modifier
                    .height(200.dp)
                    .width(180.dp),
                    movie = movie,
                    onMovieClick = {navigateToDetail(movie)}
                )
            }
        })
    }

}
