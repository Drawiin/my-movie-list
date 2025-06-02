package com.drawiin.mymovielist.feature.moviedetails.presentation

import androidx.lifecycle.viewModelScope
import com.drawiin.mymovielist.core.arch.MyMovieListViewModel
import com.drawiin.mymovielist.feature.moviedetails.domain.model.MovieDetailsModel
import com.drawiin.mymovielist.feature.moviedetails.domain.usercase.GetMovieDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MovieDetailState {
    data object Loading : MovieDetailState
    data class Success(val movieDetails: MovieDetailsModel) : MovieDetailState
    data class Error(val message: String) : MovieDetailState
}

sealed interface MovieDetailsSideEffect {
    data object GoBack : MovieDetailsSideEffect
}

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val initial: MovieDetailState
) : MyMovieListViewModel<MovieDetailState, MovieDetailsSideEffect>(initial) {

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            updateState { MovieDetailState.Loading }
            getMovieDetailsUseCase.invoke(movieId)
                .onSuccess { updateState { MovieDetailState.Success(it) } }
                .onFailure { updateState { MovieDetailState.Error(it.message.toString()) } }
        }
    }

    fun onBackPressed() {
        dispatchSideEffect(MovieDetailsSideEffect.GoBack)
    }
}
