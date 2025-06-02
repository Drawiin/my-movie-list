package com.drawiin.mymovielist.feature.moviedetails.data.repository

import com.drawiin.mymovielist.feature.moviedetails.domain.model.MovieDetailsModel

interface MovieDetailsRepository {
    suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsModel>
}
