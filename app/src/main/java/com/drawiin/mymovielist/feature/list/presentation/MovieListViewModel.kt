package com.drawiin.mymovielist.feature.list.presentation

import androidx.lifecycle.viewModelScope
import com.drawiin.mymovielist.common.movie.domain.model.MoviePreviewModel
import com.drawiin.mymovielist.core.arch.MyMovieListViewModel
import com.drawiin.mymovielist.feature.list.domain.usecase.GetMovieListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

sealed interface MovieListState {
    data object Loading : MovieListState
    data class Success(val movies: List<MoviePreviewModel> = emptyList()) : MovieListState
    data class Error(val message: String) : MovieListState
}

sealed interface MyMoviesListSideEffect {
    data class GoToMovieDetails(val movieId: Int) : MyMoviesListSideEffect
}

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getMoviesUseCase: GetMovieListUseCase,
    private val initial: MovieListState
) : MyMovieListViewModel<MovieListState, MyMoviesListSideEffect>(initial = initial){
    fun fetchMovies() {
        viewModelScope.launch {
            updateState { MovieListState.Loading }
            getMoviesUseCase.invoke()
                .onSuccess { updateState { MovieListState.Success(it) } }
                .onFailure { updateState { MovieListState.Error(it.message.toString()) } }
        }
    }

    fun onMovieClicked(movieId: Int) {
        dispatchSideEffect(MyMoviesListSideEffect.GoToMovieDetails(movieId))
    }
}
