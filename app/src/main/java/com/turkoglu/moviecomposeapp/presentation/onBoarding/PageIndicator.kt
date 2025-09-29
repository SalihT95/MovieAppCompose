package com.turkoglu.moviecomposeapp.presentation.onBoarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PageIndicator(pageSize: Int, currentPage: Int) {
    Row(
        modifier = Modifier.wrapContentSize(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageSize) { index ->
            val color = if (index == currentPage) {
                Color.White // AKTİF NOKTA: Parlak Beyaz
            } else {
                Color.White.copy(alpha = 0.4f) // SOLUK NOKTA: Yarı şeffaf beyaz
            }
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}