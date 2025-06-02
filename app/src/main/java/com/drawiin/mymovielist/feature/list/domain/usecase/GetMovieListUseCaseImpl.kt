package com.drawiin.mymovielist.feature.list.domain.usecase

import com.drawiin.mymovielist.common.movie.domain.model.MoviePreviewModel
import com.drawiin.mymovielist.feature.list.data.repository.MovieListRepository
import javax.inject.Inject

class GetMovieListUseCaseImpl @Inject constructor(
    val repository: MovieListRepository
): GetMovieListUseCase {
    override suspend fun invoke(): Result<List<MoviePreviewModel>> {
        return repository.getMovies()
    }
}
