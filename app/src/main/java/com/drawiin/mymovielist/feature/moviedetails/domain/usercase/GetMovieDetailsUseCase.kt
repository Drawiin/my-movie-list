package com.drawiin.mymovielist.feature.moviedetails.domain.usercase

import com.drawiin.mymovielist.common.movie.domain.model.MovieModel

fun interface GetMovieDetailsUseCase {
    suspend operator fun invoke(movieId: Int): Result<MovieModel>
}
