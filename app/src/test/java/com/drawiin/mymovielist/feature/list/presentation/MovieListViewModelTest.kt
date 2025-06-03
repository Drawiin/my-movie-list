package com.drawiin.mymovielist.feature.list.presentation

import com.drawiin.mymovielist.core.test.InstantDispatcherRule
import com.drawiin.mymovielist.core.test.assertIsEqualTo
import com.drawiin.mymovielist.core.test.viewModelTest
import com.drawiin.mymovielist.feature.list.domain.model.MovieListPageModel
import com.drawiin.mymovielist.feature.list.domain.model.MoviePreviewModel
import com.drawiin.mymovielist.feature.list.domain.usecase.GetMovieListUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieListViewModelTest {
    @get:Rule
    val dispatcherRule = InstantDispatcherRule()
    private lateinit var movieListViewModel: MovieListViewModel

    @MockK(relaxed = true)
    private lateinit var getMoviesUseCase: GetMovieListUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        movieListViewModel = MovieListViewModel(
            getMoviesUseCase = getMoviesUseCase,
            initial = MovieListState()
        )
    }

    @Test
    fun `should show error when repository returns error`() = viewModelTest(movieListViewModel) {
        coEvery { getMoviesUseCase.invoke(1) } returns Result.failure(Exception("error"))

        viewModelUnderTest.fetchMovies()
        advanceUntilIdle()

        states.last() assertIsEqualTo MovieListState(errorMessage = "error")
    }

    @Test
    fun `should update state with movies on successful fetch`() = viewModelTest(movieListViewModel) {
        val movies = listOf(MoviePreviewModel(1, "Movie", "2024-01-01", "url"))
        val result = MovieListPageModel(page = 1, totalPages = 2, movies = movies)
        coEvery { getMoviesUseCase.invoke(1) } returns Result.success(result)

        viewModelUnderTest.fetchMovies()
        advanceUntilIdle()

        states.last() assertIsEqualTo MovieListState(
            movies = movies,
            currentPage = 1,
            totalPages = 2,
            isLoading = false,
            errorMessage = null
        )
    }

    @Test
    fun `should append movies and increment page on onLoadMore`() = viewModelTest(movieListViewModel) {
        val firstPage = MovieListPageModel(
            page = 1,
            totalPages = 2,
            movies = listOf(MoviePreviewModel(1, "A", "2024-01-01", "url"))
        )
        val secondPage = MovieListPageModel(
            page = 2,
            totalPages = 2,
            movies = listOf(MoviePreviewModel(2, "B", "2024-01-02", "url"))
        )
        coEvery { getMoviesUseCase.invoke(1) } returns Result.success(firstPage)
        coEvery { getMoviesUseCase.invoke(2) } returns Result.success(secondPage)

        viewModelUnderTest.fetchMovies()
        advanceUntilIdle()
        movieListViewModel.onLoadMore()
        advanceUntilIdle()

        states.last() assertIsEqualTo MovieListState(
            movies = firstPage.movies + secondPage.movies,
            currentPage = 2,
            totalPages = 2,
            isLoading = false,
            errorMessage = null
        )
    }

    @Test
    fun `should not load more if already at last page`() = viewModelTest(movieListViewModel) {
        val result = MovieListPageModel(
            page = 1, totalPages = 1,
            movies = listOf(
                MoviePreviewModel(
                    1, "title", "date",
                    bannerUrl = "url"
                )
            )
        )
        coEvery { getMoviesUseCase.invoke(1) } returns Result.success(result)

        viewModelUnderTest.fetchMovies()
        advanceUntilIdle()
        viewModelUnderTest.onLoadMore()
        advanceUntilIdle()

        // State should not change (still at page 1)
        states.last() assertIsEqualTo MovieListState(
            movies = result.movies,
            currentPage = 1,
            totalPages = 1,
            isLoading = false,
            errorMessage = null
        )
    }

    @Test
    fun `should emit GoToMovieDetails side effect on movie click`() = viewModelTest(movieListViewModel) {
        viewModelUnderTest.onMovieClicked(42)
        advanceUntilIdle()

        sideEffects.last() assertIsEqualTo MyMoviesListSideEffect.GoToMovieDetails(42)
    }

    @Test
    fun `should emit GoToWatchList side effect on go to watchlist`() = viewModelTest(movieListViewModel) {
        viewModelUnderTest.onGoToWatchList()
        advanceUntilIdle()

        sideEffects.last() assertIsEqualTo MyMoviesListSideEffect.GoToWatchList
    }

    @Test
    fun `should filter out duplicated movies when loading more pages`() = viewModelTest(movieListViewModel) {
        val movieA = MoviePreviewModel(1, "A", "2024-01-01", "url")
        val movieB = MoviePreviewModel(2, "B", "2024-01-02", "url")
        val firstPage = MovieListPageModel(
            page = 1,
            totalPages = 2,
            movies = listOf(movieA, movieB)
        )
        // Second page contains a duplicate of movieB and a new movieC
        val movieC = MoviePreviewModel(3, "C", "2024-01-03", "url")
        val secondPage = MovieListPageModel(
            page = 2,
            totalPages = 2,
            movies = listOf(movieB, movieC)
        )
        coEvery { getMoviesUseCase.invoke(1) } returns Result.success(firstPage)
        coEvery { getMoviesUseCase.invoke(2) } returns Result.success(secondPage)

        viewModelUnderTest.fetchMovies()
        advanceUntilIdle()
        viewModelUnderTest.onLoadMore()
        advanceUntilIdle()

        // Only movieC should be appended, movieB is filtered out as duplicate
        states.last() assertIsEqualTo MovieListState(
            movies = listOf(movieA, movieB, movieC),
            currentPage = 2,
            totalPages = 2,
            isLoading = false,
            errorMessage = null
        )
    }
}
