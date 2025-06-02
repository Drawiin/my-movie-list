package com.drawiin.mymovielist.feature.list.data.datasource

import com.drawiin.mymovielist.core.network.client.MyMovieListNetworkClient
import com.drawiin.mymovielist.feature.list.data.dto.MovieListResponseDTO
import javax.inject.Inject

class MovieListRemoteDataSourceImpl @Inject constructor(
    val client: MyMovieListNetworkClient
): MovieListRemoteDataSource {
    override suspend fun getPopularMovies(): Result<MovieListResponseDTO> {
        return client.get(
            "popular?language=en-US&page=1",
            MovieListResponseDTO::class
        )
    }
}
