package com.drawiin.mymovielist.feature.moviedetails.presentation

import androidx.lifecycle.viewModelScope
import com.drawiin.mymovielist.common.movie.domain.model.MovieModel
import com.drawiin.mymovielist.common.movie.domain.usecase.AddMovieToWatchListUseCase
import com.drawiin.mymovielist.common.movie.domain.usecase.CheckMovieInWatchListUseCase
import com.drawiin.mymovielist.common.movie.domain.usecase.RemoveMovieFromWatchListUseCase
import com.drawiin.mymovielist.core.arch.MyMovieListViewModel
import com.drawiin.mymovielist.feature.moviedetails.domain.usercase.GetMovieDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MovieDetailState {
    data object Loading : MovieDetailState
    data class Success(val movieDetails: MovieModel) : MovieDetailState
    data class Error(val message: String) : MovieDetailState
}

sealed interface MovieDetailsSideEffect {
    data object GoBack : MovieDetailsSideEffect
    data object NotifyWatchListAdd : MovieDetailsSideEffect
    data object NotifyWatchListRemove : MovieDetailsSideEffect
}

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val addMovieToWatchListUseCase: AddMovieToWatchListUseCase,
    private val removeMovieFromWatchListUseCase: RemoveMovieFromWatchListUseCase,
    private val checkMovieInWatchListUseCase: CheckMovieInWatchListUseCase,
    private val initial: MovieDetailState
) : MyMovieListViewModel<MovieDetailState, MovieDetailsSideEffect>(initial) {

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            updateState { MovieDetailState.Loading }
            getMovieDetailsUseCase.invoke(movieId)
                .map {
                    it.copy(
                        isInWatchList = this@MovieDetailsViewModel.checkMovieInWatchListUseCase(
                            it.id
                        )
                    )
                }
                .onSuccess {
                    updateState {
                        MovieDetailState.Success(it)
                    }
                }
                .onFailure {
                    updateState {
                        MovieDetailState.Error(it.message.toString())
                    }
                }
        }
    }

    fun onBackPressed() {
        dispatchSideEffect(MovieDetailsSideEffect.GoBack)
    }

    fun onAddToWatchList() {
        when (val currentState = state.value) {
            is MovieDetailState.Success -> if (currentState.movieDetails.isInWatchList) {
                viewModelScope.launch {
                    removeMovieFromWatchListUseCase(currentState.movieDetails.id)
                        .onSuccess {
                            updateState {
                                currentState.copy(currentState.movieDetails.copy(isInWatchList = false))
                            }
                            dispatchSideEffect(MovieDetailsSideEffect.NotifyWatchListRemove)
                        }
                }
            } else {
                viewModelScope.launch {
                    addMovieToWatchListUseCase(currentState.movieDetails)
                        .onSuccess {
                            updateState {
                                currentState.copy(currentState.movieDetails.copy(isInWatchList = true))
                            }
                            dispatchSideEffect(MovieDetailsSideEffect.NotifyWatchListAdd)
                        }
                }
            }

            else -> return // No need to proceed if not in Success state
        }
    }
}
