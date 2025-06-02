package com.drawiin.mymovielist.feature.moviedetails.data.datasource

import com.drawiin.mymovielist.core.network.client.MyMovieListNetworkClient
import com.drawiin.mymovielist.feature.moviedetails.data.dto.MovieDetailsDTO
import javax.inject.Inject

class MovieDetailsRemoteDataSourceImpl @Inject constructor(
    val networkClient: MyMovieListNetworkClient
): MovieDetailsRemoteDataSource {
    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsDTO> {
        return networkClient.get("$movieId?language=en-US", MovieDetailsDTO::class)
    }
}
