package com.drawiin.mymovielist.feature.moviedetails.presentation

import com.drawiin.mymovielist.common.movie.domain.model.MovieModel
import com.drawiin.mymovielist.common.movie.domain.usecase.AddMovieToWatchListUseCase
import com.drawiin.mymovielist.common.movie.domain.usecase.CheckMovieInWatchListUseCase
import com.drawiin.mymovielist.common.movie.domain.usecase.RemoveMovieFromWatchListUseCase
import com.drawiin.mymovielist.core.test.InstantDispatcherRule
import com.drawiin.mymovielist.core.test.assertIsEqualTo
import com.drawiin.mymovielist.core.test.viewModelTest
import com.drawiin.mymovielist.feature.moviedetails.domain.usercase.GetMovieDetailsUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsViewModelTest {
    @get:Rule
    val dispatcherRule = InstantDispatcherRule()

    @MockK(relaxed = true)
    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase

    @MockK(relaxed = true)
    private lateinit var addMovieToWatchListUseCase: AddMovieToWatchListUseCase

    @MockK(relaxed = true)
    private lateinit var removeMovieFromWatchListUseCase: RemoveMovieFromWatchListUseCase

    @MockK(relaxed = true)
    private lateinit var checkMovieInWatchListUseCase: CheckMovieInWatchListUseCase

    private lateinit var movieDetailsViewModel: MovieDetailsViewModel

    private val movie = MovieModel(
        id = 1,
        isInWatchList = false,
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

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        movieDetailsViewModel = MovieDetailsViewModel(
            getMovieDetailsUseCase,
            addMovieToWatchListUseCase,
            removeMovieFromWatchListUseCase,
            checkMovieInWatchListUseCase,
            initial = MovieDetailState.Loading
        )
    }

    @Test
    fun `should emit Loading then Success on successful fetch`() =
        viewModelTest(movieDetailsViewModel) {
            coEvery { getMovieDetailsUseCase.invoke(1) } returns Result.success(movie)
            coEvery { checkMovieInWatchListUseCase.invoke(1) } returns true

            viewModelUnderTest.getMovieDetails(1)
            advanceUntilIdle()

            // First state is Loading, last is Success with isInWatchList = true
            states.first() assertIsEqualTo MovieDetailState.Loading
            states.last() assertIsEqualTo MovieDetailState.Success(movie.copy(isInWatchList = true))
        }

    @Test
    fun `should emit Loading then Error on failed fetch`() = viewModelTest(movieDetailsViewModel) {
        coEvery { getMovieDetailsUseCase.invoke(1) } returns Result.failure(Exception("error"))

        viewModelUnderTest.getMovieDetails(1)
        advanceUntilIdle()

        states.first() assertIsEqualTo MovieDetailState.Loading
        states.last() assertIsEqualTo MovieDetailState.Error("error")
    }

    @Test
    fun `should add to watchlist and update state and emit NotifyWatchListAdd`() = viewModelTest(
        MovieDetailsViewModel(
            getMovieDetailsUseCase,
            addMovieToWatchListUseCase,
            removeMovieFromWatchListUseCase,
            checkMovieInWatchListUseCase,
            initial = MovieDetailState.Success(movie.copy(isInWatchList = false))
        )
    ) {
        coEvery { addMovieToWatchListUseCase.invoke(any()) } returns Result.success(Unit)

        viewModelUnderTest.onAddToWatchList()
        advanceUntilIdle()

        states.last() assertIsEqualTo MovieDetailState.Success(movie.copy(isInWatchList = true))
        sideEffects.last() assertIsEqualTo MovieDetailsSideEffect.NotifyWatchListAdd
    }

    @Test
    fun `should remove from watchlist and update state and emit NotifyWatchListRemove`() =
        viewModelTest(
            MovieDetailsViewModel(
                getMovieDetailsUseCase,
                addMovieToWatchListUseCase,
                removeMovieFromWatchListUseCase,
                checkMovieInWatchListUseCase,
                initial = MovieDetailState.Success(movie.copy(isInWatchList = true))
            )
        ) {
            coEvery { removeMovieFromWatchListUseCase.invoke(any()) } returns Result.success(Unit)

            viewModelUnderTest.onAddToWatchList()
            advanceUntilIdle()

            states.last() assertIsEqualTo MovieDetailState.Success(movie.copy(isInWatchList = false))
            sideEffects.last() assertIsEqualTo MovieDetailsSideEffect.NotifyWatchListRemove
        }

    @Test
    fun `should emit GoBack side effect on back pressed`() = viewModelTest(movieDetailsViewModel) {
        viewModelUnderTest.onBackPressed()
        advanceUntilIdle()

        sideEffects.last() assertIsEqualTo MovieDetailsSideEffect.GoBack
    }
}
