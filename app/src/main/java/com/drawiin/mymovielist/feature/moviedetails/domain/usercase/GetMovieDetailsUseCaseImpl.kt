package com.drawiin.mymovielist.feature.moviedetails.domain.usercase

import com.drawiin.mymovielist.feature.moviedetails.data.repository.MovieDetailsRepository
import com.drawiin.mymovielist.feature.moviedetails.domain.model.MovieDetailsModel
import javax.inject.Inject

class GetMovieDetailsUseCaseImpl @Inject constructor(
    val repository: MovieDetailsRepository
) : GetMovieDetailsUseCase {
    override suspend fun invoke(movieId: Int): Result<MovieDetailsModel> {
        return repository.getMovieDetails(movieId)
    }
}
