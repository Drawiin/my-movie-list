package com.drawiin.mymovielist.common.movie.data.repository

import com.drawiin.mymovielist.common.movie.data.datasource.local.MovieDao
import com.drawiin.mymovielist.common.movie.data.entity.MovieEntity
import com.drawiin.mymovielist.common.movie.data.entity.toDomainModel
import com.drawiin.mymovielist.common.movie.domain.model.MovieModel
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val localDataSource: MovieDao
) : MovieRepository {
    override suspend fun saveMovie(movie: MovieModel): Result<Unit> {
        return runCatching {
            localDataSource.insert(MovieEntity.fromModel(movie))
        }
    }

    override suspend fun getMovieById(movieId: Int): Result<MovieModel> {
        return runCatching {
            localDataSource.getById(movieId)
        }.mapCatching {
            it?.toDomainModel() ?: throw NoSuchElementException("Movie with id $movieId not found")
        }
    }

    override suspend fun getAllMovies(): Result<List<MovieModel>> {
        return runCatching {
            localDataSource.getAll()
        }.mapCatching {
            it.map { it.toDomainModel() }
        }
    }

    override suspend fun deleteMovie(movieId: Int): Result<Unit> {
        return runCatching {
            localDataSource.delete(movieId)
        }
    }
}
