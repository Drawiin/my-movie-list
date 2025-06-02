package com.drawiin.mymovielist.feature.moviedetails.data.repository

import com.drawiin.mymovielist.feature.moviedetails.data.datasource.MovieDetailsRemoteDataSource
import com.drawiin.mymovielist.feature.moviedetails.data.dto.toDomainModel
import com.drawiin.mymovielist.feature.moviedetails.domain.model.MovieDetailsModel
import javax.inject.Inject

class MovieDetailsRepositoryImpl @Inject constructor(
    private val remoteDataSource: MovieDetailsRemoteDataSource
): MovieDetailsRepository {
    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsModel> {
        return remoteDataSource.getMovieDetails(movieId).map {
            it.toDomainModel()
        }
    }
}
