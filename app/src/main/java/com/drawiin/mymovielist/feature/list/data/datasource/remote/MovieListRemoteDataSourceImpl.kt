package com.drawiin.mymovielist.feature.list.data.datasource.remote

import com.drawiin.mymovielist.core.network.client.MyMovieListNetworkClient
import com.drawiin.mymovielist.feature.list.data.dto.MovieListResponseDTO
import javax.inject.Inject

class MovieListRemoteDataSourceImpl @Inject constructor(
    val client: MyMovieListNetworkClient
): MovieListRemoteDataSource {
    override suspend fun getPopularMovies(page: Int): Result<MovieListResponseDTO> {
        return client.get(
            "popular?language=en-US&page=${page}",
            MovieListResponseDTO::class
        )
    }
}
