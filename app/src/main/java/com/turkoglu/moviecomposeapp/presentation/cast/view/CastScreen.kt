package com.turkoglu.moviecomposeapp.presentation.cast.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.turkoglu.moviecomposeapp.domain.model.MovieCast
import com.turkoglu.moviecomposeapp.domain.model.PersonDetail
import com.turkoglu.moviecomposeapp.presentation.cast.CastScreenViewModel
import com.turkoglu.moviecomposeapp.presentation.component.CircularBackButtons
import com.turkoglu.moviecomposeapp.presentation.ui.AccentYellow
import com.turkoglu.moviecomposeapp.presentation.ui.AppBackgroundGradient
import com.turkoglu.moviecomposeapp.presentation.ui.GrayTextSecondary

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CastScreen(
    navController: NavController,
    viewModel: CastScreenViewModel = hiltViewModel()
) {

    val state = viewModel.state.value
    val person = state.person
    val movies = state.movies
    val isLoading = state.isLoading
    val errorMessage = state.error

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(person?.name ?: "Cast Detail") },
                navigationIcon = {
                    CircularBackButtons(
                        onBackClick = { navController.popBackStack() },
                        onHomeClick = { navController.navigate("Home") })
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackgroundGradient)
                .padding(paddingValues)
        ) {
            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                errorMessage != null -> Text(
                    text = errorMessage,
                    modifier = Modifier.align(Alignment.Center)
                )

                person != null -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            PersonHeader(person)
                        }
                        if (person.biography.isNotBlank()) {
                            item {
                                Text(
                                    text = "Biography",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Text(
                                    text = person.biography,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                        if (movies.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Known For",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            items(movies) { movie ->
                                MovieCastItem(movie, onItemClick = {
                                    navController.navigate("Detail/${movie.id}")
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PersonHeader(person: PersonDetail) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = "https://image.tmdb.org/t/p/w300${person.profilePath}"
            ),
            contentDescription = person.name,
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = person.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            person.knownForDepartment?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrayTextSecondary
                )
            }
            person.placeOfBirth?.let {
                Text(
                    text = "Born in $it",
                    style = MaterialTheme.typography.bodySmall,
                    color = GrayTextSecondary
                )
            }
            person.birthday?.let {
                Text(
                    text = "Birthday: $it",
                    style = MaterialTheme.typography.bodySmall,
                    color = GrayTextSecondary
                )
            }
        }
    }
}

@Composable
fun MovieCastItem(movie: MovieCast, onItemClick: (MovieCast) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(movie) }
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = "https://image.tmdb.org/t/p/w185${movie.posterPath}"
            ),
            contentDescription = movie.title,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            movie.character?.let {
                Text(
                    text = "as $it",
                    style = MaterialTheme.typography.bodySmall,
                    color = GrayTextSecondary
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = AccentYellow,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "%.1f".format(movie.voteAverage),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
