package com.turkoglu.composedeneme.presentation.detail.view

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.turkoglu.composedeneme.presentation.detail.CastState
import com.turkoglu.composedeneme.presentation.detail.DetailScreenViewModel
import com.turkoglu.composedeneme.presentation.fav.FavViewModel
import com.turkoglu.composedeneme.util.Constants

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable()
fun DetailScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DetailScreenViewModel = hiltViewModel(),
    viewModelFav : FavViewModel =  hiltViewModel()

) {
    val scrollState = rememberLazyListState()
    val state = viewModel.state.value
    val castState = viewModel.castState
    val fragmanState = viewModel.fragmanState

    Column(modifier = modifier.fillMaxSize()) {
        Text(text = state.title)
        Spacer(modifier = modifier.padding(20.dp))
        Text(text = state.overview)
    }
    Box {
        FilmInfo(
            scrollState = scrollState,
            overview = state.overview,
            releaseDate = state.releaseDate,
            state = castState.value
        )
        FilmImageBanner(
            rating = state.voteAverage.toFloat(),
            key= fragmanState.value.videoUrl!!,
            viewModel = viewModel,
            navController = navController,
            viewModelFav = viewModelFav,
            posterUrl = "${Constants.IMAGE_BASE_URL}/${viewModel.state.value.posterPath}",
            filmName = viewModel.state.value.title,
            filmId = viewModel.state.value.imdbId,
            releaseDate = viewModel.state.value.releaseDate
        )
    }
}