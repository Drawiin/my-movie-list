package com.drawiin.mymovielist.common.movie.domain.usecase

import com.drawiin.mymovielist.common.movie.domain.model.MovieModel

fun interface GetWatchListUseCase {
    suspend operator fun invoke(): Result<List<MovieModel>>
}
