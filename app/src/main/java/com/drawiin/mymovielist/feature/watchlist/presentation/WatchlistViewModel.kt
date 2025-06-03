package com.drawiin.mymovielist.feature.watchlist.presentation

import androidx.lifecycle.viewModelScope
import com.drawiin.mymovielist.common.movie.domain.model.MovieModel
import com.drawiin.mymovielist.common.movie.domain.usecase.GetWatchListUseCase
import com.drawiin.mymovielist.core.arch.MyMovieListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface WatchListState {
    data object Loading : WatchListState
    data class Success(val movies: List<MovieModel>) : WatchListState
    data class Error(val message: String) : WatchListState
}

sealed interface WatchListSideEffect {
    data object GoBack : WatchListSideEffect
    data class NavigateToMovieDetails(val id: Int) : WatchListSideEffect
}

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val getWatchListUseCase: GetWatchListUseCase,
    private val initial: WatchListState
) : MyMovieListViewModel<WatchListState, WatchListSideEffect>(initial) {
    fun getWatchList() {
        viewModelScope.launch {
            updateState { WatchListState.Loading }
            getWatchListUseCase()
                .onSuccess { movies ->
                    updateState { WatchListState.Success(movies) }
                }
                .onFailure { error ->
                    updateState { WatchListState.Error(error.message.toString()) }
                }
        }
    }

    fun onGoBack() {
        dispatchSideEffect(WatchListSideEffect.GoBack)
    }

    fun onMovieClick(movieId: Int) {
        dispatchSideEffect(WatchListSideEffect.NavigateToMovieDetails(movieId))
    }
}
