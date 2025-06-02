package com.drawiin.mymovielist.core.uikit.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drawiin.mymovielist.core.uikit.foundation.MyMovieListTheme

@Composable
fun MoviePreviewCard(
    modifier: Modifier = Modifier,
    title: String,
    posterUrl: String,
    releaseDate: String,
    onMovieClicked: () -> Unit = {}
) {
    Column(modifier = modifier.clickable {
        onMovieClicked()
    }) {
        MoviePosterImage(posterUrl = posterUrl)
        Column(modifier = Modifier.padding(8.dp)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(releaseDate, style = MaterialTheme.typography.bodySmall)
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun MoviePreviewCardPreview() {
    MyMovieListTheme {
        MoviePreviewCard(
            title = "Lilo & Stitch",
            posterUrl = "https://image.tmdb.org/t/p/w500/3bN675X0K2E5QiAZVChzB5wq90B.jpg",
            releaseDate = "2025-05-17",
        )
    }
}


