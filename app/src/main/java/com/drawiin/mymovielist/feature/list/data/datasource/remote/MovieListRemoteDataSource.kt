package com.drawiin.mymovielist.feature.list.data.datasource.remote

import com.drawiin.mymovielist.feature.list.data.dto.MovieListResponseDTO

interface MovieListRemoteDataSource {
    suspend fun getPopularMovies(page: Int): Result<MovieListResponseDTO>
}
