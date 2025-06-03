package com.drawiin.mymovielist.feature.moviedetails.data.repository

import com.drawiin.mymovielist.common.movie.data.repository.MovieRepository
import com.drawiin.mymovielist.common.movie.domain.model.MovieModel
import com.drawiin.mymovielist.feature.moviedetails.data.datasource.remote.MovieDetailsRemoteDataSource
import com.drawiin.mymovielist.feature.moviedetails.data.dto.toDomainModel
import javax.inject.Inject

class MovieDetailsRepositoryImpl @Inject constructor(
    private val remoteDataSource: MovieDetailsRemoteDataSource,
) : MovieDetailsRepository {
    override suspend fun getMovieDetails(movieId: Int): Result<MovieModel> {
        return remoteDataSource.getMovieDetails(movieId).map {
            it.toDomainModel()
        }
    }
}
