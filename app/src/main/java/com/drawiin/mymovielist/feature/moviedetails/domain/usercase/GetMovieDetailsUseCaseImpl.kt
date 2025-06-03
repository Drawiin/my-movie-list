package com.drawiin.mymovielist.feature.moviedetails.domain.usercase

import com.drawiin.mymovielist.feature.moviedetails.data.repository.MovieDetailsRepository
import com.drawiin.mymovielist.common.movie.domain.model.MovieModel
import javax.inject.Inject

class GetMovieDetailsUseCaseImpl @Inject constructor(
    val repository: MovieDetailsRepository
) : GetMovieDetailsUseCase {
    override suspend fun invoke(movieId: Int): Result<MovieModel> {
        return repository.getMovieDetails(movieId)
    }
}
