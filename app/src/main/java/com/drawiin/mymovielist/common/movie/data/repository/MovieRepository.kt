package com.drawiin.mymovielist.common.movie.data.repository

import com.drawiin.mymovielist.common.movie.domain.model.MovieModel

interface MovieRepository {
    suspend fun saveMovie(movie: MovieModel): Result<Unit>
    suspend fun getMovieById(movieId: Int): Result<MovieModel>
    suspend fun getAllMovies(): Result<List<MovieModel>>
    suspend fun deleteMovie(movieId: Int): Result<Unit>
}
