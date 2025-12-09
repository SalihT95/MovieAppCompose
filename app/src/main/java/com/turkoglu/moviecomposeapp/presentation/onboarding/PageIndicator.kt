package com.turkoglu.moviecomposeapp.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PageIndicator(pageSize: Int, currentPage: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageSize) { page ->
            // Seçili sayfa için Primary, diğerleri için Beyazın biraz şeffaf hali
            // Çünkü arka planımız artık koyu renkli bir resim/gölge.
            val color = if (page == currentPage)
                MaterialTheme.colorScheme.primary
            else
                Color.White.copy(alpha = 0.5f)

            Box(
                modifier = Modifier
                    .size(if (page == currentPage) 14.dp else 10.dp) // Seçili olan biraz daha büyük
                    .clip(CircleShape)
                    .background(color = color)
            )
        }
    }
}