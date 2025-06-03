package com.drawiin.mymovielist.feature.list.presentation

import androidx.lifecycle.viewModelScope
import com.drawiin.mymovielist.core.arch.MyMovieListViewModel
import com.drawiin.mymovielist.feature.list.domain.model.MoviePreviewModel
import com.drawiin.mymovielist.feature.list.domain.usecase.GetMovieListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

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
        if (state.value.currentPage < state.value.totalPages) {
            viewModelScope.launch {
                updateState { copy(isLoading = true) }
                getMoviesUseCase(state.value.currentPage + 1)
                    .onSuccess { newPage ->
                        updateState {
                            copy(
                                movies = movies + newPage.movies,
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
