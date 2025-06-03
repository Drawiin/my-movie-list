package com.drawiin.mymovielist.feature.watchlist.presentation

import com.drawiin.mymovielist.common.movie.domain.model.MovieModel
import com.drawiin.mymovielist.common.movie.domain.usecase.GetWatchListUseCase
import com.drawiin.mymovielist.core.test.InstantDispatcherRule
import com.drawiin.mymovielist.core.test.assertIsEqualTo
import com.drawiin.mymovielist.core.test.viewModelTest
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WatchlistViewModelTest {
    @get:Rule
    val dispatcherRule = InstantDispatcherRule()

    @MockK(relaxed = true)
    private lateinit var getWatchListUseCase: GetWatchListUseCase

    private lateinit var watchListViewModel: WatchlistViewModel

    private val movies = listOf(
        MovieModel(
            id = 1,
            isInWatchList = true,
            title = "Test Movie",
            overview = "Overview",
            releaseDate = "2024-01-01",
            genres = listOf("Action"),
            posterUrl = "poster",
            backdropUrl = "backdrop",
            voteAverage = 8.0,
            voteCount = 100,
            budget = 1000000,
            revenue = 2000000,
            runtime = 120,
            status = "Released",
            tagline = "Tagline"
        )
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        watchListViewModel = WatchlistViewModel(
            getWatchListUseCase = getWatchListUseCase,
            initial = WatchListState.Loading
        )
    }

    @Test
    fun `should emit Loading then Success on successful fetch`() = viewModelTest(watchListViewModel) {
        coEvery { getWatchListUseCase.invoke() } returns Result.success(movies)

        viewModelUnderTest.getWatchList()
        advanceUntilIdle()

        states.first() assertIsEqualTo WatchListState.Loading
        states.last() assertIsEqualTo WatchListState.Success(movies)
    }

    @Test
    fun `should emit Loading then Error on failed fetch`() = viewModelTest(watchListViewModel) {
        coEvery { getWatchListUseCase.invoke() } returns Result.failure(Exception("error"))

        viewModelUnderTest.getWatchList()
        advanceUntilIdle()

        states.first() assertIsEqualTo WatchListState.Loading
        states.last() assertIsEqualTo WatchListState.Error("error")
    }

    @Test
    fun `should emit GoBack side effect on go back`() = viewModelTest(watchListViewModel) {
        viewModelUnderTest.onGoBack()
        advanceUntilIdle()

        sideEffects.last() assertIsEqualTo WatchListSideEffect.GoBack
    }

    @Test
    fun `should emit NavigateToMovieDetails side effect on movie click`() = viewModelTest(watchListViewModel) {
        viewModelUnderTest.onMovieClick(42)
        advanceUntilIdle()

        sideEffects.last() assertIsEqualTo WatchListSideEffect.NavigateToMovieDetails(42)
    }
}
