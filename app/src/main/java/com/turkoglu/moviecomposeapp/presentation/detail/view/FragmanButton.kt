package com.turkoglu.moviecomposeapp.presentation.detail.view

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FragmanButton(
    backgroundColor: Color = Color.Red.copy(alpha = 0.7f),
    iconTint: Color = MaterialTheme.colors.onBackground,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(70.dp)
            .height(40.dp),
        shape = CircleShape,
        color = backgroundColor,
        elevation = 4.dp
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share Trailer",
                tint = iconTint
            )
        }
    }
}
