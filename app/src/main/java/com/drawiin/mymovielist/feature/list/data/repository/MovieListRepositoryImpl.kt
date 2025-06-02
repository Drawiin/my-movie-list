package com.drawiin.mymovielist.feature.list.data.repository

import com.drawiin.mymovielist.common.movie.domain.model.MoviePreviewModel
import com.drawiin.mymovielist.feature.list.data.datasource.MovieListRemoteDataSource
import com.drawiin.mymovielist.feature.list.data.dto.toDomainModel
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val remoteDataSource: MovieListRemoteDataSource
) : MovieListRepository {
    override suspend fun getMovies(): Result<List<MoviePreviewModel>> {
        return remoteDataSource.getPopularMovies().map {
            it.results.map { movie ->
                movie.toDomainModel()
            }
        }
    }
}
