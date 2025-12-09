package com.turkoglu.moviecomposeapp.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.turkoglu.moviecomposeapp.R

@Composable
fun CastItem(
    castImageUrl: String,
    castName: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(80.dp)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(castImageUrl)
                .placeholder(R.drawable.user) // Placeholder (kendi kaynağına göre düzenle)
                .error(R.drawable.user)
                .crossfade(true)
                .build(),
            contentDescription = "Oyuncu: $castName",
            contentScale = ContentScale.Crop, // Resmi daireye sığacak şekilde kırpar
            modifier = Modifier
                .size(70.dp) // Daire boyutu
                .clip(CircleShape) // Resmi yuvarlak yapar
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = castName,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            maxLines = 2, // İsim uzunsa 2 satıra sığsın
            overflow = TextOverflow.Ellipsis
        )
    }
}