package com.drawiin.mymovielist.feature.list.presentation

import com.drawiin.mymovielist.core.test.InstantDispatcherRule
import com.drawiin.mymovielist.feature.list.domain.GetMovieListUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieListViewModelTest {
    @get:Rule
    val dispatcherRule = InstantDispatcherRule()
    private lateinit var viwModel: MovieListViewModel
    private lateinit var getMoviesUseCase: GetMovieListUseCase

    @Before
    fun setup() {
        // TODO - Use a mock library like MockK or Mockito to create a mock repository
        getMoviesUseCase = GetMovieListUseCase {
            Result.failure(Exception("error"))
        }
        viwModel = MovieListViewModel(
            getMoviesUseCase = getMoviesUseCase,
        )
    }

    @Test
    fun `should show error when repository returns error`() = runTest {
        val states = mutableListOf<MovieListState>()
        val job = launch(UnconfinedTestDispatcher()) {
            viwModel.state.collect { states.add(it) }
        }
        viwModel.fetchMovies()
        advanceUntilIdle()

        assert(states.last() == MovieListState.Error("error")) {
            "Expected error state, but got ${states.last()}"
        }
        job.cancel()
    }
}
