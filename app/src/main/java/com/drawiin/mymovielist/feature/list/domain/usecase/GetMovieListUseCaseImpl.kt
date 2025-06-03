package com.drawiin.mymovielist.feature.list.domain.usecase

import com.drawiin.mymovielist.feature.list.domain.model.MoviePreviewModel
import com.drawiin.mymovielist.feature.list.data.repository.MovieListRepository
import com.drawiin.mymovielist.feature.list.domain.model.MovieListPageModel
import javax.inject.Inject

class GetMovieListUseCaseImpl @Inject constructor(
    val repository: MovieListRepository
): GetMovieListUseCase {
    override suspend fun invoke(page: Int): Result<MovieListPageModel> {
        return repository.getMovies(page)
    }
}
