package com.drawiin.mymovielist.common.movie.domain.usecase

import com.drawiin.mymovielist.common.movie.data.repository.MovieRepository
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
class RemoveMovieFromWatchListUseCaseImplTest {

    @MockK(relaxed = true)
    private lateinit var repository: MovieRepository

    private lateinit var useCase: RemoveMovieFromWatchListUseCaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = RemoveMovieFromWatchListUseCaseImpl(repository)
    }

    @Test
    fun `should return success when repository deletes movie`() = runTest {
        coEvery { repository.deleteMovie(1) } returns Result.success(Unit)

        val result = useCase.invoke(1)

        assertTrue(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
    }

    @Test
    fun `should return failure when repository returns error`() = runTest {
        val exception = Exception("error")
        coEvery { repository.deleteMovie(1) } returns Result.failure(exception)

        val result = useCase.invoke(1)

        assertTrue(result.isFailure)
        assertEquals("error", result.exceptionOrNull()?.message)
    }
}
