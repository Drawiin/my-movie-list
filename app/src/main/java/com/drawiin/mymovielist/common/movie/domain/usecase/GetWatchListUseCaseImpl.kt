package com.drawiin.mymovielist.common.movie.domain.usecase

import com.drawiin.mymovielist.common.movie.data.repository.MovieRepository
import com.drawiin.mymovielist.common.movie.domain.model.MovieModel
import javax.inject.Inject

class GetWatchListUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository,
) : GetWatchListUseCase {
    override suspend fun invoke(): Result<List<MovieModel>> {
        return movieRepository.getAllMovies()
    }
}
