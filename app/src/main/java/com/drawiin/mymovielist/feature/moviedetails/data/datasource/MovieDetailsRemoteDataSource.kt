package com.drawiin.mymovielist.feature.moviedetails.data.datasource

import com.drawiin.mymovielist.feature.moviedetails.data.dto.MovieDetailsDTO

interface MovieDetailsRemoteDataSource {
    suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsDTO>
}
