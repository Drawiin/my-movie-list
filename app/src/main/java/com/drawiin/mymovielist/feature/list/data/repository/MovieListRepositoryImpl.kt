package com.drawiin.mymovielist.feature.list.data.repository

import com.drawiin.mymovielist.feature.list.data.datasource.remote.MovieListRemoteDataSource
import com.drawiin.mymovielist.feature.list.data.dto.toDomainModel
import com.drawiin.mymovielist.feature.list.domain.model.MovieListPageModel
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val remoteDataSource: MovieListRemoteDataSource
) : MovieListRepository {
    override suspend fun getMovies(page: Int): Result<MovieListPageModel> {
        return remoteDataSource.getPopularMovies(page).map {
            it.toDomainModel()
        }
    }
}
