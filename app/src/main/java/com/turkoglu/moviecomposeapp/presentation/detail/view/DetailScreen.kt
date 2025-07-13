package com.turkoglu.moviecomposeapp.presentation.detail.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.turkoglu.moviecomposeapp.data.local.Favorite
import com.turkoglu.moviecomposeapp.presentation.component.CastItem
import com.turkoglu.moviecomposeapp.presentation.component.CircularBackButtons
import com.turkoglu.moviecomposeapp.presentation.component.CircularFavoriteButtons
import com.turkoglu.moviecomposeapp.presentation.component.FragmanButton
import com.turkoglu.moviecomposeapp.presentation.component.GenreChipsRow
import com.turkoglu.moviecomposeapp.presentation.detail.DetailScreenViewModel
import com.turkoglu.moviecomposeapp.presentation.fav.FavViewModel
import com.turkoglu.moviecomposeapp.util.Constants
import kotlinx.coroutines.launch

@SuppressLint("SupportAnnotationUsage")
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: DetailScreenViewModel = hiltViewModel(),
    viewModelFav: FavViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val film = viewModel.state.value
    val intent = Intent(Intent.ACTION_VIEW, viewModel.fragmanState.value.videoUrl!!.toUri())
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}
    val favorite = viewModelFav.getAFavorite(film.imdbId).collectAsStateWithLifecycle(null).value
    val isFavorite = favorite != null
    val castState = viewModel.castState.value

    Box(modifier = Modifier.fillMaxSize()) {

        // Blurred background image
        AsyncImage(
            model = viewModel.state.value.posterPath,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .blur(20.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // ÜST BAR - SABİT
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                        contentDescription = "Back",
//                        tint = Color.White
//                    )
//                }
                CircularBackButtons {
                    navController.popBackStack()
                }

                FragmanButton(onClick = { launcher.launch(intent) })
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
                                        mediaId = film.imdbId,
                                        image = film.safePosterPath,
                                        title = film.title,
                                        releaseDate = film.safeReleaseDate,
                                        rating = film.voteAverage.toFloat()
                                    )
                                )
                                Toast.makeText(context, "Favorilere eklendi", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 12.dp)
                    .verticalScroll(rememberScrollState())  // Scrollable içerik
            ) {
                AsyncImage(
                    model = viewModel.state.value.posterPath,
                    contentDescription = viewModel.state.value.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = viewModel.state.value.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "⭐ %.1f".format(viewModel.state.value.voteAverage), color = Color.Yellow)
                    Text(text = viewModel.state.value.releaseDate, color = Color.LightGray)
                }

                Spacer(modifier = Modifier.height(8.dp))

                GenreChipsRow(textColor = MaterialTheme.colorScheme.onPrimary,genreList = viewModel.state.value.genres.map { it.name }) {
                    navController.navigate("ViewAll/$it")
                }

//                Text(
//                    text = "Genres: ${viewModel.state.value.genres.joinToString(", ") { it.name }}",
//                    color = Color.LightGray,
//                    fontSize = 14.sp
//                )
//                Spacer(modifier = Modifier.height(8.dp))

                ExpandableText(
                    text = viewModel.state.value.overview,
                    modifier = Modifier.fillMaxWidth(),
                    minimizedMaxLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Cast",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                LazyRow {
                    itemsIndexed(castState.cast, key = { _, cast -> cast.id }) { _, cast ->
                        CastItem(
                            modifier = Modifier,
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
@Composable
fun ExpandableText1(
    text: String,
    modifier: Modifier = Modifier,
    minimizedMaxLines: Int = 3
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = text,
            maxLines = if (expanded) Int.MAX_VALUE else minimizedMaxLines,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 13.sp
        )
        TextButton(
            onClick = { expanded = !expanded },
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = if (expanded) "Daha az" else "...Daha fazla",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 13.sp
            )
        }
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    minimizedMaxLines: Int = 3,
) {
    var cutText by remember(text) { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val textLayoutResultState = remember { mutableStateOf<TextLayoutResult?>(null) }
    val seeMoreSizeState = remember { mutableStateOf<IntSize?>(null) }
    val seeMoreOffsetState = remember { mutableStateOf<Offset?>(null) }

    val textLayoutResult = textLayoutResultState.value
    val seeMoreSize = seeMoreSizeState.value
    val seeMoreOffset = seeMoreOffsetState.value

    LaunchedEffect(text, expanded, textLayoutResult, seeMoreSize) {
        val lastLineIndex = minimizedMaxLines - 1
        if (!expanded && textLayoutResult != null && seeMoreSize != null
            && lastLineIndex + 1 == textLayoutResult.lineCount
            && textLayoutResult.isLineEllipsized(lastLineIndex)
        ) {
            var lastCharIndex = textLayoutResult.getLineEnd(lastLineIndex, visibleEnd = true) + 1
            var charRect: Rect
            do {
                lastCharIndex -= 1
                charRect = textLayoutResult.getCursorRect(lastCharIndex)
            } while (
                charRect.left > textLayoutResult.size.width - seeMoreSize.width
            )
            seeMoreOffsetState.value = Offset(charRect.left, charRect.bottom - seeMoreSize.height)
            cutText = text.substring(startIndex = 0, endIndex = lastCharIndex)
        }
    }

    Box(modifier) {
        Text(
            color = Color.DarkGray,
            text = cutText ?: text,
            fontSize = 13.sp,
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    expanded = false
                },
            maxLines = if (expanded) Int.MAX_VALUE else minimizedMaxLines,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResultState.value = it },
        )
        if (!expanded) {
            val density = LocalDensity.current
            Text(
                color = Color.Magenta,
                text = "... See more",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                onTextLayout = { seeMoreSizeState.value = it.size },
                modifier = Modifier
                    .then(
                        if (seeMoreOffset != null)
                            Modifier.offset(
                                x = with(density) { seeMoreOffset.x.toDp() },
                                y = with(density) { seeMoreOffset.y.toDp() },
                            )
                        else Modifier
                    )
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        expanded = true
                        cutText = null
                    }
                    .alpha(if (seeMoreOffset != null) 1f else 0f)
            )
        }
    }
}