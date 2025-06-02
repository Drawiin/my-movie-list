package com.drawiin.mymovielist.feature.list.presentation

import SubscribeToSideEffects
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.drawiin.mymovielist.R.string
import com.drawiin.mymovielist.common.movie.domain.model.mockMoviePreviewList
import com.drawiin.mymovielist.core.arch.OnStartSideEffect
import com.drawiin.mymovielist.core.uikit.components.BodyError
import com.drawiin.mymovielist.core.uikit.components.BodyLoading
import com.drawiin.mymovielist.core.uikit.components.MoviePreviewCard

@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel = viewModel(),
    onGoToMovieDetails: (Int) -> Unit = { }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    viewModel.sideEffects.SubscribeToSideEffects {
        when (it) {
            is MyMoviesListSideEffect.GoToMovieDetails -> onGoToMovieDetails(it.movieId)
        }
    }

    OnStartSideEffect {
        viewModel.fetchMovies()
    }

    MovieListContent(
        state = state,
        onRetry = viewModel::fetchMovies,
        onMovieClicked = viewModel::onMovieClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieListContent(
    state: MovieListState,
    onRetry: () -> Unit,
    onMovieClicked: (Int) -> Unit = {}
) {
    val gridState = rememberLazyGridState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(string.feature_list_popular_movies)) },
            )
        }
    ) { padding ->
        when (state) {
            is MovieListState.Error -> BodyError(message = state.message, onRetry = onRetry)
            is MovieListState.Loading -> BodyLoading()
            is MovieListState.Success -> LazyVerticalGrid(
                modifier = Modifier.padding(padding),
                columns = GridCells.Fixed(count = 3),
                state = gridState
            ) {
                items(state.movies, key = { it.id }) { movie ->
                    MoviePreviewCard(
                        modifier = Modifier.padding(8.dp),
                        title = movie.title,
                        posterUrl = movie.bannerUrl,
                        releaseDate = movie.date,
                        onMovieClicked = { onMovieClicked(movie.id) }
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
