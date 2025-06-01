package com.drawiin.mymovielist.feature.list.domain

import com.drawiin.mymovielist.common.movie.domain.model.MoviePreviewModel
import com.drawiin.mymovielist.feature.list.data.MovieListRepository

fun interface GetMovieListUseCase {
    suspend operator fun invoke(): Result<List<MoviePreviewModel>>
}

class GetMovieListUseCaseImpl(
    val repository: MovieListRepository
): GetMovieListUseCase {
    override suspend fun invoke(): Result<List<MoviePreviewModel>> {
        return repository.getMovies()
    }
}
