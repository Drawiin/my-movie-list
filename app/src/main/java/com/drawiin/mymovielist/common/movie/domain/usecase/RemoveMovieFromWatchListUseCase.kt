package com.drawiin.mymovielist.common.movie.domain.usecase

import com.drawiin.mymovielist.common.movie.domain.model.MovieModel

fun interface RemoveMovieFromWatchListUseCase {
    suspend operator fun invoke(movieId: Int): Result<Unit>
}
