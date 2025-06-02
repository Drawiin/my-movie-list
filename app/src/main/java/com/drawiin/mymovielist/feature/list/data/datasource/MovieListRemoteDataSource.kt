package com.drawiin.mymovielist.feature.list.data.datasource

import com.drawiin.mymovielist.feature.list.data.dto.MovieListResponseDTO

interface MovieListRemoteDataSource {
    suspend fun getPopularMovies(): Result<MovieListResponseDTO>
}
