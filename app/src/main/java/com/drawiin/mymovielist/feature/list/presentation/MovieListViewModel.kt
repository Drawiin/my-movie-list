package com.drawiin.mymovielist.feature.list.presentation

import androidx.lifecycle.viewModelScope
import com.drawiin.mymovielist.core.arch.MyMovieListViewModel
import com.drawiin.mymovielist.feature.list.domain.model.MoviePreviewModel
import com.drawiin.mymovielist.feature.list.domain.usecase.GetMovieListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import kotlin.math.min

data class MovieListState(
    val movies: List<MoviePreviewModel> = emptyList(),
    val currentPage: Int = 1,
    val totalPages: Int = 1,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface MyMoviesListSideEffect {
    data class GoToMovieDetails(val movieId: Int) : MyMoviesListSideEffect
    data object GoToWatchList : MyMoviesListSideEffect
}

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getMoviesUseCase: GetMovieListUseCase,
    private val initial: MovieListState
) : MyMovieListViewModel<MovieListState, MyMoviesListSideEffect>(initial = initial) {
    private val fetchMoviesMutex = Mutex()

    fun fetchMovies() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            getMoviesUseCase(state.value.currentPage)
                .onSuccess {
                    updateState {
                        copy(
                            movies = it.movies,
                            currentPage = it.page,
                            totalPages = it.totalPages,
                        )
                    }
                }
                .onFailure { updateState { copy(errorMessage = it.message.toString()) } }
            updateState { copy(isLoading = false) }
        }
    }

    fun onLoadMore() {
        viewModelScope.launch {

            // Use mutex to prevent concurrent fetches
            fetchMoviesMutex.withLock {
                val state = state.value
                val nextPage = state.currentPage + 1
                val totalPages = state.totalPages

                if (nextPage > totalPages) return@launch

                updateState { copy(isLoading = true) }
                delay(2000L) // Add a delay here to avoid loading pages too quickly
                getMoviesUseCase(state.currentPage + 1)
                    .onSuccess { newPage ->
                        updateState {
                            copy(
                                // Sometimes TmDB API returns the same movies in the next page, so we filter them out
                                movies = movies + newPage.movies.filterNot { it in movies.takeLast(
                                    min(10, movies.size)  // Limit to last 10 movies to avoid duplicates
                                ) },
                                currentPage = newPage.page,
                                totalPages = newPage.totalPages,
                            )
                        }
                    }
                    .onFailure { updateState { copy(errorMessage = it.message.toString()) } }
                updateState { copy(isLoading = false) }
            }

        }
    }

    fun onMovieClicked(movieId: Int) {
        dispatchSideEffect(MyMoviesListSideEffect.GoToMovieDetails(movieId))
    }

    fun onGoToWatchList() {
        dispatchSideEffect(MyMoviesListSideEffect.GoToWatchList)
    }


}
