package com.drawiin.mymovielist.core.arch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
open class MyMovieListViewModel<S, E>(private val initial: S) : ViewModel() {
    private val _state = MutableStateFlow(initial)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _sideEffects = SingleSharedFlow<E>()
    val sideEffects: SharedFlow<E> = _sideEffects

    protected fun updateState(stateReducer: S.() -> S) {
        _state.update(stateReducer)
    }

    protected fun getAndUpdateState(stateReducer: S.() -> S): S {
        return _state.getAndUpdate(stateReducer)
    }

    protected fun dispatchSideEffect(sideEffect: E) {
        viewModelScope.launch {
            _sideEffects.emit(sideEffect)
        }
    }
}
