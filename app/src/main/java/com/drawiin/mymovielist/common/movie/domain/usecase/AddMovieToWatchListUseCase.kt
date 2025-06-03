package com.drawiin.mymovielist.common.movie.domain.usecase

import com.drawiin.mymovielist.common.movie.domain.model.MovieModel

fun interface AddMovieToWatchListUseCase {
    suspend operator fun invoke(movie: MovieModel): Result<Unit>
}
