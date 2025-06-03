package com.drawiin.mymovielist.feature.list.presentation

import SubscribeToSideEffects
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.drawiin.mymovielist.R.string
import com.drawiin.mymovielist.core.arch.OnStartSideEffect
import com.drawiin.mymovielist.core.uikit.components.BodyError
import com.drawiin.mymovielist.core.uikit.components.BodyLoading
import com.drawiin.mymovielist.core.uikit.components.MoviePreviewCard
import com.drawiin.mymovielist.feature.list.domain.model.mockMoviePreviewList
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel = viewModel(),
    onGoToMovieDetails: (Int) -> Unit = { },
    onGoToWatchList: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    viewModel.sideEffects.SubscribeToSideEffects {
        when (it) {
            is MyMoviesListSideEffect.GoToMovieDetails -> onGoToMovieDetails(it.movieId)
            is MyMoviesListSideEffect.GoToWatchList -> onGoToWatchList()
        }
    }

    OnStartSideEffect {
        viewModel.fetchMovies()
    }

    MovieListContent(
        state = state,
        onRetry = viewModel::fetchMovies,
        onMovieClicked = viewModel::onMovieClicked,
        onGoToWatchList = viewModel::onGoToWatchList,
        onLoadMore = viewModel::onLoadMore
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieListContent(
    state: MovieListState,
    onRetry: () -> Unit,
    onMovieClicked: (Int) -> Unit = {},
    onGoToWatchList: () -> Unit = {},
    onLoadMore: () -> Unit = {},
) {
    val gridState = rememberLazyGridState()
    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo }
            .collectLatest { layoutInfo ->
                val totalItems = layoutInfo.totalItemsCount
                val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                if (lastVisible >= totalItems - 1) {
                    onLoadMore()
                }
            }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(string.feature_list_popular_movies)) },
                actions = {
                    IconButton(onClick = onGoToWatchList) {
                        Icon(
                            Icons.AutoMirrored.Default.List,
                            contentDescription = stringResource(string.feature_watch_list)
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (state.errorMessage != null && !state.isLoading && state.movies.isEmpty()) {
            BodyError(
                message = state.errorMessage,
                onRetry = onRetry
            )
            return@Scaffold
        }

        if (state.isLoading && state.movies.isEmpty()) {
            BodyLoading()
            return@Scaffold
        }

        LazyVerticalGrid(
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
            if (state.isLoading) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            if (state.errorMessage != null) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "An error occurred: ${state.errorMessage}",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.error
                            )
                        )
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun MovieListContentPreview() {
    MovieListContent(
        state = MovieListState(movies = mockMoviePreviewList, isLoading = true),
        onRetry = {}
    )
}
