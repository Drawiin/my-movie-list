package com.drawiin.mymovielist.common.movie.domain.usecase

import com.drawiin.mymovielist.common.movie.data.repository.MovieRepository
import com.drawiin.mymovielist.common.movie.domain.model.MovieModel
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
class GetWatchListUseCaseImplTest {

    @MockK(relaxed = true)
    private lateinit var repository: MovieRepository

    private lateinit var useCase: GetWatchListUseCaseImpl

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
        useCase = GetWatchListUseCaseImpl(repository)
    }

    @Test
    fun `should return success when repository returns movies`() = runTest {
        coEvery { repository.getAllMovies() } returns Result.success(movies)

        val result = useCase.invoke()

        assertTrue(result.isSuccess)
        assertEquals(movies, result.getOrNull())
    }

    @Test
    fun `should return failure when repository returns error`() = runTest {
        val exception = Exception("error")
        coEvery { repository.getAllMovies() } returns Result.failure(exception)

        val result = useCase.invoke()

        assertTrue(result.isFailure)
        assertEquals("error", result.exceptionOrNull()?.message)
    }
}
