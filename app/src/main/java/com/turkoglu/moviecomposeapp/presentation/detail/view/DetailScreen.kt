package com.turkoglu.moviecomposeapp.presentation.detail.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.turkoglu.moviecomposeapp.R
import com.turkoglu.moviecomposeapp.domain.model.Favorite
import com.turkoglu.moviecomposeapp.presentation.Screen
import com.turkoglu.moviecomposeapp.presentation.component.CastItem
import com.turkoglu.moviecomposeapp.presentation.component.CircularBackButtons
import com.turkoglu.moviecomposeapp.presentation.component.CircularFavoriteButtons
import com.turkoglu.moviecomposeapp.presentation.component.ExpandableText
import com.turkoglu.moviecomposeapp.presentation.component.FragmanButton
import com.turkoglu.moviecomposeapp.presentation.component.GenreChipsRow
import com.turkoglu.moviecomposeapp.presentation.detail.DetailScreenViewModel
import com.turkoglu.moviecomposeapp.presentation.fav.FavViewModel
import com.turkoglu.moviecomposeapp.presentation.ui.AppBackgroundGradient
import com.turkoglu.moviecomposeapp.presentation.user.UserViewModel
import com.turkoglu.moviecomposeapp.util.Constants
import kotlinx.coroutines.launch

@SuppressLint("SupportAnnotationUsage", "StateFlowValueCalledInComposition")
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: DetailScreenViewModel = hiltViewModel(),
    viewModelFav: FavViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val film = viewModel.state.value
    val intent = Intent(Intent.ACTION_VIEW, viewModel.fragmanState.value.videoUrl!!.toUri())
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}
    val favorite = viewModelFav.getAFavorite(film.imdbId).collectAsStateWithLifecycle(null).value
    val isFavorite = favorite != null
    val castState = viewModel.castState.value
    val currentUser = userViewModel.currentUser.collectAsState().value


    Box(modifier = Modifier
        .fillMaxSize()
        .background(AppBackgroundGradient)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CircularBackButtons { navController.popBackStack() }
                FragmanButton { launcher.launch(intent) }
                CircularFavoriteButtons(
                    isLiked = isFavorite,
                    onClick = { isFav ->
                        coroutineScope.launch {
                            if (currentUser == null || currentUser.isGuest) {
                                // Guest ise favori ekleyemesin
                                Toast.makeText(
                                    context,
                                    "Favorilere eklemek için giriş yapmalısınız",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate(Screen.Login.route) // Login ekranına yönlendir
                                return@launch
                            }

                            if (isFav) {
                                favorite?.let {
                                    viewModelFav.deleteOneFavorite(it)
                                    Toast.makeText(
                                        context,
                                        "Favorilerden kaldırıldı",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                viewModelFav.insertFavorite(
                                    Favorite(
                                        favorite = true,
                                        mediaId = film.imdbId,
                                        image = film.safePosterPath,
                                        title = film.title,
                                        releaseDate = film.safeReleaseDate,
                                        rating = film.voteAverage.toFloat()
                                    )
                                )
                                Toast.makeText(context, "Favorilere eklendi", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 12.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(film.posterPath)
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder)
                        .crossfade(true)
                        .build(),
                    contentDescription = film.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(20.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = film.title,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "⭐ %.1f".format(film.voteAverage), color = Color.Yellow)
                    Text(
                        text = film.releaseDate,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                GenreChipsRow(
                    textColor = MaterialTheme.colorScheme.onBackground,
                    genreList = film.genres.map { it.name }
                ) {
                    navController.navigate("ViewAll/$it")
                }

                ExpandableText(
                    text = film.overview,
                    modifier = Modifier.fillMaxWidth(),
                    minimizedMaxLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Cast",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    itemsIndexed(castState.cast, key = { _, cast -> cast.id }) { _, cast ->
                        CastItem(
                            modifier = Modifier.clickable {
                                navController.navigate(
                                    Screen.Cast.route.replace(
                                        "{personId}",
                                        cast.id.toString()
                                    )
                                )
                            },
                            castImageUrl = "${Constants.IMAGE_BASE_URL}/${cast.profile_path}",
                            castName = cast.name
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
