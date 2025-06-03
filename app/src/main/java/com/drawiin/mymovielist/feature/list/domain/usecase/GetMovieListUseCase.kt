package com.drawiin.mymovielist.feature.list.domain.usecase

import com.drawiin.mymovielist.feature.list.domain.model.MovieListPageModel

fun interface GetMovieListUseCase {
    suspend operator fun invoke(page: Int): Result<MovieListPageModel>
}

