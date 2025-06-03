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
class AddMovieToWatchListUseCaseImplTest {

    @MockK(relaxed = true)
    private lateinit var repository: MovieRepository

    private lateinit var useCase: AddMovieToWatchListUseCaseImpl

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
        useCase = AddMovieToWatchListUseCaseImpl(repository)
    }

    @Test
    fun `should return success when repository saves movie`() = runTest {
        coEvery { repository.saveMovie(movie) } returns Result.success(Unit)

        val result = useCase.invoke(movie)

        assertTrue(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
    }

    @Test
    fun `should return failure when repository returns error`() = runTest {
        val exception = Exception("error")
        coEvery { repository.saveMovie(movie) } returns Result.failure(exception)

        val result = useCase.invoke(movie)

        assertTrue(result.isFailure)
        assertEquals("error", result.exceptionOrNull()?.message)
    }
}
