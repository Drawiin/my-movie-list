package com.drawiin.mymovielist.common.movie.domain.usecase

import com.drawiin.mymovielist.common.movie.data.repository.MovieRepository
import com.drawiin.mymovielist.common.movie.domain.model.MovieModel
import javax.inject.Inject

class AddMovieToWatchListUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
): AddMovieToWatchListUseCase {
    override suspend fun invoke(movie: MovieModel): Result<Unit> {
        return movieRepository.saveMovie(movie)
    }
}
