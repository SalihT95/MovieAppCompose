package com.turkoglu.moviecomposeapp.presentation.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenu // Menü için import
import androidx.compose.material3.DropdownMenuItem // Menü öğeleri için import
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CircularBackButtons(
    color: Color = MaterialTheme.colorScheme.onBackground,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(40.dp)
            .padding(start = 8.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.3f))
            .combinedClickable(
                onClick = onBackClick,
                onLongClick = { menuExpanded = true }
            )
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Geri Git",
            tint = color,
            modifier = Modifier.size(24.dp)
        )

        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Anasayfa") },
                onClick = {
                    menuExpanded = false
                    onHomeClick()
                }
            )

            DropdownMenuItem(
                text = { Text("Geri Dön") },
                onClick = {
                    menuExpanded = false
                    onBackClick()
                }
            )
        }
    }
}