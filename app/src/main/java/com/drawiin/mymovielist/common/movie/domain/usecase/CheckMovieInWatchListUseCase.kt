package com.drawiin.mymovielist.common.movie.domain.usecase

fun interface CheckMovieInWatchListUseCase {
    suspend operator fun invoke(movieId: Int): Boolean
}
