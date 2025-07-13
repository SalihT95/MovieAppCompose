package com.turkoglu.moviecomposeapp.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.turkoglu.moviecomposeapp.domain.model.Movie

@Composable
fun MovieListItem(
    movie: Movie,
    modifier: Modifier = Modifier,
    onItemClick: (Movie) -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable { onItemClick(movie) },
        tonalElevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
                //.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w342${movie.posterPath}",
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 3f)
                    .clip(MaterialTheme.shapes.medium)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = movie.title ?: "No Title",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = movie.releaseDate ?: "No release date",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

//@Composable
//fun MovieListItem(
//    movie: Movie,
//    modifier: Modifier = Modifier,
//    onItemClick: () -> Unit
//) {
//    Card(
//        modifier = modifier
//            .clickable { onItemClick() },
//        shape = RoundedCornerShape(12.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
//    ) {
//        Box(modifier = Modifier.fillMaxSize()) {
//            // Film posteri (image)
//            AsyncImage(
//                model = movie.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" },
//                contentDescription = movie.title,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier.fillMaxSize()
//            )
//
//            // Üstte, solda rating göstergesi küçük bir badge olarak
//            Surface(
//                shape = RoundedCornerShape(bottomEnd = 12.dp),
//                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
//                modifier = Modifier
//                    .padding(6.dp)
//                    .align(Alignment.TopStart)
//            ) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
//                ) {
//                    Icon(
//                        imageVector = Icons.Rounded.Star,
//                        contentDescription = "Rating",
//                        tint = Color.Yellow,
//                        modifier = Modifier.size(14.dp)
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//                    Text(
//                        text = String.format("%.1f", movie.voteAverage),
//                        style = MaterialTheme.typography.labelSmall,
//                        color = MaterialTheme.colorScheme.onPrimary
//                    )
//                }
//            }
//
//            // Altta film adı arka plan ile
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(
//                        Brush.verticalGradient(
//                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
//                        )
//                    )
//                    .align(Alignment.BottomStart)
//                    .padding(8.dp)
//            ) {
//                Text(
//                    text = movie.title ?: "No title",
//                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
//                    color = Color.White,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis
//                )
//            }
//        }
//    }
//}