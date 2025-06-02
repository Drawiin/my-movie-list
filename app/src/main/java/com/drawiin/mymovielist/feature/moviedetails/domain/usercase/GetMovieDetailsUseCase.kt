package com.drawiin.mymovielist.feature.moviedetails.domain.usercase

import com.drawiin.mymovielist.feature.moviedetails.domain.model.MovieDetailsModel

fun interface GetMovieDetailsUseCase {
    suspend operator fun invoke(movieId: Int): Result<MovieDetailsModel>
}
