package com.drawiin.mymovielist.feature.moviedetails.presentation

import SubscribeToSideEffects
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.drawiin.mymovielist.R
import com.drawiin.mymovielist.core.arch.OnStartSideEffect
import com.drawiin.mymovielist.core.uikit.components.BackDropImage
import com.drawiin.mymovielist.core.uikit.components.BodyError
import com.drawiin.mymovielist.core.uikit.components.BodyLoading
import com.drawiin.mymovielist.core.uikit.components.MoviePosterImage

@Composable
fun MovieDetailScreen(
    viewModel: MovieDetailsViewModel,
    movieId: Int,
    onGoBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    viewModel.sideEffects.SubscribeToSideEffects {
        when (it) {
            is MovieDetailsSideEffect.GoBack -> onGoBack()
        }
    }

    OnStartSideEffect {
        viewModel.getMovieDetails(movieId)
    }

    MovieDetailContent(
        state,
        onGoBack = onGoBack,
        onRetry = { viewModel.getMovieDetails(movieId) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailContent(state: MovieDetailState, onGoBack: () -> Unit, onRetry: () -> Unit) {
    val lazyListState = rememberLazyListState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Movie Details") },
                navigationIcon = {
                    IconButton(onClick = onGoBack) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, null)
                    }
                })
        }
    ) { padding ->
        when (state) {
            is MovieDetailState.Loading -> BodyLoading()
            is MovieDetailState.Error -> BodyError(message = state.message, onRetry = onRetry)
            is MovieDetailState.Success -> LazyColumn(
                modifier = Modifier.padding(padding),
                state = lazyListState
            ) {
                item(MovieDetailsSection.Header) {
                    HeaderSection(state)
                }
                item(MovieDetailsSection.Genres) {
                    GenresSection(state)
                }
                item(MovieDetailsSection.Overview) {
                    OverviewSection(state)
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(state: MovieDetailState.Success) {
    Column {
        Box(Modifier.fillMaxWidth()) {
            BackDropImage(
                modifier = Modifier,
                backDropUrl = state.movieDetails.backdropUrl
            )
            MoviePosterImage(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxWidth(0.3f)
                    .padding(start = 16.dp),
                posterUrl = state.movieDetails.posterUrl
            )
        }
        Column(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
        ) {
            Text(
                text = state.movieDetails.title,
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = state.movieDetails.releaseDate,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun GenresSection(state: MovieDetailState.Success) {
    Row(
        Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        state.movieDetails.genres.forEach {
            SuggestionChip(
                onClick = { },
                label = { Text(it) }
            )
        }
    }
}

@Composable
private fun OverviewSection(state: MovieDetailState.Success) {
    Column(Modifier.padding(horizontal = 16.dp)) {
        Text(
            stringResource(R.string.feature_details_overview),
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            state.movieDetails.overview,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

enum class MovieDetailsSection {
    Header,
    Overview,
    Genres,
}

@Composable
@Preview(showBackground = true)
fun MovieDetailContentPreview() {
    MovieDetailContent(
        state = MovieDetailState.Success(
            movieDetails = com.drawiin.mymovielist.feature.moviedetails.domain.model.MovieDetailsModel(
                id = 1,
                title = "Lilo & Stitch",
                overview = "A lonely Hawaiian girl and a fugitive alien mend a broken family.",
                releaseDate = "2025-05-17",
                genres = listOf("Family", "Comedy", "Science Fiction"),
                posterUrl = "",
                backdropUrl = "",
                voteAverage = 7.1,
                voteCount = 366,
                budget = 100_000_000,
                revenue = 610_800_000,
                runtime = 108,
                status = "Released",
                tagline = "Hold on to your coconuts."
            )
        ),
        onGoBack = {},
        onRetry = {}
    )
}
