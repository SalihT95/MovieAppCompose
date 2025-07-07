package com.turkoglu.moviecomposeapp.presentation.fav.view

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.turkoglu.moviecomposeapp.R
import com.turkoglu.moviecomposeapp.data.local.Favorite
import com.turkoglu.moviecomposeapp.presentation.detail.view.VoteAverageRatingIndicator
import com.turkoglu.moviecomposeapp.presentation.fav.FavViewModel
import com.turkoglu.moviecomposeapp.presentation.ui.Transparent
import com.turkoglu.moviecomposeapp.presentation.ui.primaryDark
import com.turkoglu.moviecomposeapp.presentation.ui.primaryPink

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavScreen(
    navController: NavController,
    viewModel: FavViewModel = hiltViewModel()
) {
    val openDialog = remember { mutableStateOf(false) }
    val favoriteFilms by viewModel.favorites.collectAsStateWithLifecycle(emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Favorites",
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp
            )
            IconButton(onClick = { openDialog.value = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete All",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        if (favoriteFilms.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.size(250.dp),
                    painter = painterResource(id = R.drawable.ic_empty_cuate),
                    contentDescription = "Empty State"
                )
            }
        } else {
            LazyColumn {
                itemsIndexed(
                    items = favoriteFilms,
                    key = { _, favorite -> favorite.mediaId }
                ) { _, favorite ->
                    val dismissState = rememberDismissState()

                    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                        LaunchedEffect(favorite) {
                            viewModel.deleteOneFavorite(favorite)
                        }
                    }

                    SwipeToDismiss(
                        state = dismissState,
                        modifier = Modifier.padding(vertical = 4.dp),
                        directions = setOf(DismissDirection.EndToStart),
                        dismissThresholds = { direction ->
                            FractionalThreshold(if (direction == DismissDirection.EndToStart) 0.1f else 0.05f)
                        },
                        background = {
                            val color by animateColorAsState(
                                if (dismissState.targetValue == DismissValue.Default) primaryDark else primaryPink,
                                label = ""
                            )
                            val scale by animateFloatAsState(
                                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f,
                                label = ""
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color)
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Icon",
                                    modifier = Modifier.scale(scale)
                                )
                            }
                        },
                        dismissContent = {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(230.dp)
                                    .clickable {
                                        navController.navigate("Detail/${favorite.mediaId}")
                                    },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                FilmItem(favorite)
                            }
                        }
                    )
                }
            }
        }

        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = { openDialog.value = false },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteAllFavorites()
                            openDialog.value = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryPink)
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { openDialog.value = false },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryPink)
                    ) {
                        Text("No")
                    }
                },
                title = { Text("Delete all favorites") },
                text = { Text("Are you sure you want to delete all?") },
                containerColor = Color.White,
                textContentColor = Color.Black,
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@Composable
fun FilmItem(filmItem: Favorite) {
    Box {
        Image(
            painter = rememberAsyncImagePainter(
                model = filmItem.image,
                placeholder = painterResource(R.drawable.ic_placeholder)
            ),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            contentDescription = "Movie Banner"
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.0f to Transparent,
                        1f to primaryDark
                    )
                )
        )
        FilmDetails(
            title = filmItem.title,
            releaseDate = filmItem.releaseDate,
            rating = filmItem.rating
        )
    }
}

@Composable
fun FilmDetails(title: String, releaseDate: String, rating: Float) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = releaseDate,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
            VoteAverageRatingIndicator(percentage = rating)
        }
    }
}