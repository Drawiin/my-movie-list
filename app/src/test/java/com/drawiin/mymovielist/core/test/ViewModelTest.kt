package com.drawiin.mymovielist.core.test

import androidx.compose.runtime.mutableStateListOf
import com.drawiin.mymovielist.core.arch.MyMovieListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@ExperimentalCoroutinesApi
inline fun <S, E, VM : MyMovieListViewModel<S, E>> viewModelTest(
    viewModel: VM,
    crossinline test: Scope<S, E, VM>.() -> Unit,
) = runTest {
    val stateList = mutableStateListOf<S>()
    val sideEffectList = mutableStateListOf<E>()

    val collectionJobs = arrayOf(
        launch(UnconfinedTestDispatcher()) {
            viewModel.state.collect { stateList.add(it) }
        },
        launch {
            viewModel.sideEffects.collect { sideEffectList.add(it) }
        }
    )

    Scope(this, stateList, sideEffectList, viewModel).test()

    collectionJobs.forEach { job -> job.cancel() }
}

@OptIn(ExperimentalCoroutinesApi::class)
class Scope<S, E, VM>(
    val scope: TestScope,
    val states: List<S>,
    val sideEffects: List<E>,
    val viewModelUnderTest: VM
) {
    fun advanceUntilIdle() {
        scope.advanceUntilIdle()
    }
}
