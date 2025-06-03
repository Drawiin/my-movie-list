package com.drawiin.mymovielist.common.movie.domain.usecase

import com.drawiin.mymovielist.common.movie.data.repository.MovieRepository
import javax.inject.Inject

class CheckMovieInWatchListUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : CheckMovieInWatchListUseCase {
    override suspend fun invoke(movieId: Int): Boolean {
        return movieRepository.getMovieById(movieId).isSuccess
    }
}
