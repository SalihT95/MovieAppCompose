package com.turkoglu.moviecomposeapp.presentation.detail.view

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.turkoglu.moviecomposeapp.presentation.detail.DetailScreenViewModel
import com.turkoglu.moviecomposeapp.presentation.fav.FavViewModel
import com.turkoglu.moviecomposeapp.util.Constants

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun DetailScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DetailScreenViewModel = hiltViewModel(),
    viewModelFav: FavViewModel = hiltViewModel()
) {
    val scrollState = rememberLazyListState()
    val film = viewModel.state.value
    val castState = viewModel.castState.value
    val fragmanUrl = viewModel.fragmanState.value.videoUrl

    Box(modifier = modifier.fillMaxSize()) {

        FilmInfo(
            scrollState = scrollState,
            overview = film.overview,
            releaseDate = film.releaseDate,
            genrelist = film.genres,
            state = castState
        )

        fragmanUrl?.let { url ->
            FilmImageBanner(
                rating = film.voteAverage.toFloat(),
                key = url,
                viewModel = viewModel,
                navController = navController,
                viewModelFav = viewModelFav,
                posterUrl = "${Constants.IMAGE_BASE_URL}/${film.posterPath}",
                filmName = film.title,
                filmId = film.imdbId,
                releaseDate = film.releaseDate
            )
        }

    }
}