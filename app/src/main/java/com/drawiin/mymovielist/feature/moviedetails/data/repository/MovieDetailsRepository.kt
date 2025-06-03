package com.drawiin.mymovielist.feature.moviedetails.data.repository

import com.drawiin.mymovielist.common.movie.domain.model.MovieModel

interface MovieDetailsRepository {
    suspend fun getMovieDetails(movieId: Int): Result<MovieModel>
}
