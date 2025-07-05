package com.turkoglu.moviecomposeapp.presentation.detail.view

import CircularBackButtons
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.turkoglu.moviecomposeapp.data.local.Favorite
import com.turkoglu.moviecomposeapp.presentation.detail.DetailScreenViewModel
import com.turkoglu.moviecomposeapp.presentation.fav.FavViewModel
import kotlinx.coroutines.launch


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun FilmImageBanner(
    rating: Float,
    key: String,
    viewModel: DetailScreenViewModel,
    navController: NavController,
    posterUrl: String,
    filmName: String,
    filmId: Int,
    releaseDate: String,
    viewModelFav: FavViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = viewModel.state.value
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(key))
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(420.dp)
    ) {
        AsyncImage(
            model = state.posterPath,
            contentDescription = "Movie Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        FilmNameAndRating(
            filmName = state.title,
            rating = rating
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 10.dp)
        ) {
            CircularBackButtons(
                onClick = { navController.popBackStack() }
            )
            FragmanButton(
                onClick = { launcher.launch(intent) }
            )
            CircularFavoriteButtons(
                isLiked = viewModelFav.isAFavorite(filmId).observeAsState().value != null,
                onClick = { isFav ->
                    coroutineScope.launch {
                        if (isFav) {
                            Toast.makeText(
                                context,
                                "Already added to your favorites",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            viewModelFav.insertFavorite(
                                Favorite(
                                    favorite = true,
                                    mediaId = filmId,
                                    image = posterUrl,
                                    title = filmName,
                                    releaseDate = releaseDate,
                                    rating = rating
                                )
                            )
                        }
                    }
                }
            )
        }
    }
}