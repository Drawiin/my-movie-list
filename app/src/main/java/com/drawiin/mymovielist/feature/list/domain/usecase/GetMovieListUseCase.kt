package com.drawiin.mymovielist.feature.list.domain.usecase

import com.drawiin.mymovielist.common.movie.domain.model.MoviePreviewModel

fun interface GetMovieListUseCase {
    suspend operator fun invoke(): Result<List<MoviePreviewModel>>
}

