package com.drawiin.mymovielist.core.uikit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

@Composable
fun MoviePosterImage(
    modifier: Modifier = Modifier,
    posterUrl: String
) {
    AsyncImage(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.66f)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        model = posterUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}
