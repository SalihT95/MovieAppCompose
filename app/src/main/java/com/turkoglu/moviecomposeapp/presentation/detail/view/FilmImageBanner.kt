package com.turkoglu.moviecomposeapp.presentation.detail.view

import android.content.Intent
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val intent = Intent(Intent.ACTION_VIEW, key.toUri())
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    val favorite = viewModelFav.getAFavorite(filmId).collectAsStateWithLifecycle(null).value
    val isFavorite = favorite != null

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(420.dp)
    ) {
        // Poster Görseli
        AsyncImage(
            model = state.posterPath,
            contentDescription = "Movie Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Üst Menü Butonları (Geri, Fragman, Favori)
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
                isLiked = isFavorite,
                onClick = { isFav ->
                    coroutineScope.launch {
                        if (isFav) {
                            favorite?.let {
                                viewModelFav.deleteOneFavorite(it)
                                Toast.makeText(context, "Favorilerden kaldırıldı", Toast.LENGTH_SHORT).show()
                            }
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
                            Toast.makeText(context, "Favorilere eklendi", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }

        // Alt Film Adı ve Puanı
        FilmNameAndRating(
            filmName = state.title,
            rating = rating,
            modifier = Modifier
                .align(Alignment.BottomStart)
        )
    }
}
