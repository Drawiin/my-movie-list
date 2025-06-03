package com.drawiin.mymovielist.feature.moviedetails.data.datasource.remote

import com.drawiin.mymovielist.feature.moviedetails.data.dto.MovieDetailsDTO

interface MovieDetailsRemoteDataSource {
    suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsDTO>
}
