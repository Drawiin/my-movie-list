package com.drawiin.mymovielist.feature.list.data

import com.drawiin.mymovielist.BuildConfig
import com.drawiin.mymovielist.common.movie.domain.model.MoviePreviewModel
import com.drawiin.mymovielist.core.network.NetworkClient
import com.drawiin.mymovielist.feature.list.data.dto.MovieListResponseDTO
import com.drawiin.mymovielist.feature.list.data.dto.toDomainModel

interface MovieListRepository {
    suspend fun getMovies(): Result<List<MoviePreviewModel>>
}

class MovieListRepositoryImpl(private val client: NetworkClient) : MovieListRepository {
    val token = BuildConfig.TMDB_API_TOKEN
    override suspend fun getMovies(): Result<List<MoviePreviewModel>> {
        // Simulate a network or database call
        return client.get(
            "https://api.themoviedb.org/3/movie/popular?language=en-US&page=1",
            MovieListResponseDTO::class
        ).map {
            it.results.map { movie ->
                movie.toDomainModel()
            }
        }
    }
}
