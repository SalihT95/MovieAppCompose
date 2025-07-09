package com.turkoglu.moviecomposeapp.presentation.detail.view

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun CircularFavoriteButtons(
    isLiked: Boolean,
    onClick: (isFav: Boolean) -> Unit
) {
    IconButton(
        onClick = {
            onClick(isLiked)
        }) {
        Icon(
            modifier = Modifier.size(40.dp),
            imageVector = Icons.Filled.Favorite,
            tint = if (isLiked) Color.Red else Color.LightGray,
            contentDescription = if (isLiked) "Favorilerden kaldır" else "Favorilere ekle",
        )
    }
}