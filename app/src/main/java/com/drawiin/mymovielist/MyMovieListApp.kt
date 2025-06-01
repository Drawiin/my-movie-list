package com.drawiin.mymovielist

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import com.drawiin.mymovielist.core.uikit.foundation.MyMovieListTheme
import com.drawiin.mymovielist.feature.list.presentation.MovieListScreen

@Composable
fun MyMovieListApp(modifier: Modifier = Modifier) {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }
    MyMovieListTheme {
        MovieListScreen()
    }
}
