package com.drawiin.mymovielist.core.arch

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
open class MyMovieListViewModel<S, E>(initial: S) : ViewModel() {
    private val _state = MutableStateFlow(initial)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _sideEffects = SingleSharedFlow<E>()
    val actions: SharedFlow<E> = _sideEffects

    protected fun updateState(stateReducer: S.() -> S) {
        _state.update(stateReducer)
    }

    protected fun getAndUpdateState(stateReducer: S.() -> S): S {
        return _state.getAndUpdate(stateReducer)
    }
}
