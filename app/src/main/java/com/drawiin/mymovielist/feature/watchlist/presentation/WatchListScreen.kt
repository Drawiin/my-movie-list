package com.drawiin.mymovielist.feature.watchlist.presentation

import SubscribeToSideEffects
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.drawiin.mymovielist.R.string
import com.drawiin.mymovielist.core.arch.OnStartSideEffect
import com.drawiin.mymovielist.core.uikit.components.BodyError
import com.drawiin.mymovielist.core.uikit.components.BodyLoading
import com.drawiin.mymovielist.core.uikit.components.MoviePreviewCard

@Composable
fun WatchListScreen(
    viewModel: WatchlistViewModel,
    onGoBack: () -> Unit,
    onGoToDetails: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    viewModel.sideEffects.SubscribeToSideEffects {
        when (it) {
            is WatchListSideEffect.GoBack -> onGoBack()
            is WatchListSideEffect.NavigateToMovieDetails -> onGoToDetails(it.id)
        }
    }

    OnStartSideEffect {
        viewModel.getWatchList()
    }

    BackHandler {
        viewModel.onGoBack()
    }

    WatchListContent(
        state = state,
        onGoBack = viewModel::onGoBack,
        onGoToDetails = viewModel::onMovieClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WatchListContent(state: WatchListState, onGoBack: () -> Unit, onGoToDetails: (Int) -> Unit) {
    val gridState = rememberLazyGridState()
    val isEmpty by remember(state) { derivedStateOf { (state as? WatchListState.Success)?.movies.isNullOrEmpty() } }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onGoBack) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, null)
                    }
                },
                title = { Text(stringResource(string.feature_watch_list)) },
            )
        }
    ) { padding ->
        when (state) {
            is WatchListState.Error -> BodyError(
                message = state.message,
                onRetry = {}
            )

            is WatchListState.Loading -> BodyLoading()
            is WatchListState.Success -> if (isEmpty) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(string.feature_watch_list_your_watchlist_is_empty),
                        modifier = Modifier.padding(padding),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyVerticalGrid(
                    modifier = Modifier.padding(padding),
                    columns = GridCells.Fixed(count = 3),
                    state = gridState
                ) {
                    items(state.movies, key = { it.id }) { movie ->
                        MoviePreviewCard(
                            modifier = Modifier.padding(8.dp),
                            title = movie.title,
                            posterUrl = movie.posterUrl,
                            releaseDate = movie.releaseDate,
                            onMovieClicked = { onGoToDetails(movie.id) }
                        )
                    }
                }
            }
        }
    }
}
