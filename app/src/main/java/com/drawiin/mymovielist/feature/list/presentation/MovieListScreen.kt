package com.drawiin.mymovielist.feature.list.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.drawiin.mymovielist.common.movie.domain.model.mockMoviePreviewList
import com.drawiin.mymovielist.core.uikit.components.MoviePreviewCard
import com.drawiin.mymovielist.R.string

@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel = viewModel(factory = MovieListViewModel.Factory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.fetchMovies()
    }

    MovieListContent(
        state = state,
        onRetry = viewModel::fetchMovies
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieListContent(state: MovieListState, onRetry: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(string.feature_list_popular_movies)) },
            )
        }
    ) { padding ->
        when (state) {
            is MovieListState.Error -> Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "An error occurred: ${state.message}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.error
                    )
                )
                Button(onClick = onRetry) {
                    Text(
                        "Retry",
                    )
                }
            }

            is MovieListState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is MovieListState.Success ->  LazyVerticalGrid(
                modifier = Modifier.padding(padding),
                columns = GridCells.Fixed(count = 3),
            ) {
                items(state.movies, key = { it.id }) { movie ->
                    MoviePreviewCard(
                        modifier = Modifier.padding(8.dp),
                        title = movie.title,
                        posterUrl = movie.bannerUrl,
                        releaseDate = movie.date
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieListContentPreview() {
    MovieListContent(state = MovieListState.Success(mockMoviePreviewList), onRetry = {})
}
