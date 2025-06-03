package com.drawiin.mymovielist.common.movie.domain.usecase

import com.drawiin.mymovielist.common.movie.data.repository.MovieRepository
import javax.inject.Inject

class RemoveMovieFromWatchListUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : RemoveMovieFromWatchListUseCase {
    override suspend fun invoke(movieId: Int): Result<Unit> {
        return movieRepository.deleteMovie(movieId)
    }
}
