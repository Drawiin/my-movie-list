package com.drawiin.mymovielist.feature.list.domain.usecase

import com.drawiin.mymovielist.feature.list.data.repository.MovieListRepository
import com.drawiin.mymovielist.feature.list.domain.model.MovieListPageModel
import com.drawiin.mymovielist.feature.list.domain.model.MoviePreviewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetMovieListUseCaseImplTest {

    @MockK(relaxed = true)
    private lateinit var repository: MovieListRepository

    private lateinit var useCase: GetMovieListUseCaseImpl

    private val movies = listOf(
        MoviePreviewModel(
            id = 1,
            title = "Test Movie",
            date = "2024-01-01",
            bannerUrl = "banner"
        )
    )
    private val pageModel = MovieListPageModel(
        page = 1,
        totalPages = 2,
        movies = movies
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = GetMovieListUseCaseImpl(repository)
    }

    @Test
    fun `should return success when repository returns data`() = runTest {
        coEvery { repository.getMovies(1) } returns Result.success(pageModel)

        val result = useCase.invoke(1)

        assertTrue(result.isSuccess)
        assertEquals(pageModel, result.getOrNull())
    }

    @Test
    fun `should return failure when repository returns error`() = runTest {
        val exception = Exception("error")
        coEvery { repository.getMovies(1) } returns Result.failure(exception)

        val result = useCase.invoke(1)

        assertTrue(result.isFailure)
        assertEquals("error", result.exceptionOrNull()?.message)
    }
}
