package com.drawiin.mymovielist.core.uikit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

@Composable
fun BackDropImage(modifier: Modifier = Modifier, backDropUrl: String) {
    Box(modifier) {
        AsyncImage(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(1.77f)
                .background(MaterialTheme.colorScheme.primaryContainer),
            model = backDropUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(MaterialTheme.colorScheme.scrim, Color.Transparent)
                    )
                )
        )
    }
}
