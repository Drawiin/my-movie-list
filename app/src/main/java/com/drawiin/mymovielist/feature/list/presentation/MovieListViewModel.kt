package com.drawiin.mymovielist.feature.list.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.drawiin.mymovielist.common.movie.domain.model.MoviePreviewModel
import com.drawiin.mymovielist.core.arch.MyMovieListViewModel
import com.drawiin.mymovielist.core.network.NetworkClientImpl
import com.drawiin.mymovielist.feature.list.data.MovieListRepositoryImpl
import com.drawiin.mymovielist.feature.list.domain.GetMovieListUseCase
import com.drawiin.mymovielist.feature.list.domain.GetMovieListUseCaseImpl
import kotlinx.coroutines.launch

sealed interface MovieListState {
    data object Loading : MovieListState
    data class Success(val movies: List<MoviePreviewModel> = emptyList()) : MovieListState
    data class Error(val message: String) : MovieListState
}

sealed interface MyMoviesListSideEffect {
    data class GoToMovieDetails(val movieId: String) : MyMoviesListSideEffect
}

class MovieListViewModel(
    val getMoviesUseCase: GetMovieListUseCase,
    initial: MovieListState = MovieListState.Success()
) : MyMovieListViewModel<MovieListState, MyMoviesListSideEffect>(initial = initial) {
    fun fetchMovies() {
        viewModelScope.launch {
            updateState { MovieListState.Loading }
            getMoviesUseCase.invoke()
                .onSuccess { updateState { MovieListState.Success(it) } }
                .onFailure { updateState { MovieListState.Error(it.message.toString()) } }
        }
    }

    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {

                return MovieListViewModel(
                    GetMovieListUseCaseImpl(MovieListRepositoryImpl(NetworkClientImpl()))
                ) as T
            }
        }
    }
}
