package com.drawiin.mymovielist.common.movie.domain.usecase

import com.drawiin.mymovielist.common.movie.data.repository.MovieRepository
import com.drawiin.mymovielist.common.movie.domain.model.MovieModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CheckMovieInWatchListUseCaseImplTest {

    @MockK(relaxed = true)
    private lateinit var repository: MovieRepository

    private lateinit var useCase: CheckMovieInWatchListUseCaseImpl

    private val movie = MovieModel(
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

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = CheckMovieInWatchListUseCaseImpl(repository)
    }

    @Test
    fun `should return true when repository returns movie successfully`() = runTest {
        coEvery { repository.getMovieById(1) } returns Result.success(movie)

        val result = useCase.invoke(1)

        assertTrue(result)
    }

    @Test
    fun `should return false when repository returns failure`() = runTest {
        coEvery { repository.getMovieById(1) } returns Result.failure(Exception("not found"))

        val result = useCase.invoke(1)

        assertFalse(result)
    }
}
