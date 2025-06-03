package com.drawiin.mymovielist.feature.list.data.dto

import com.drawiin.mymovielist.feature.list.domain.model.MoviePreviewModel
import com.drawiin.mymovielist.core.localization.toLocalDate
import com.drawiin.mymovielist.feature.list.domain.model.MovieListPageModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieListResponseDTO(
    @SerialName("page") val page: Int,
    @SerialName("results") val results: List<MoviePreviewDTO>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
)

@Serializable
data class MoviePreviewDTO(
    @SerialName("adult") val adult: Boolean,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("genre_ids") val genreIds: List<Int>,
    @SerialName("id") val id: Int,
    @SerialName("original_language") val originalLanguage: String,
    @SerialName("original_title") val originalTitle: String,
    @SerialName("overview") val overview: String,
    @SerialName("popularity") val popularity: Double,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("title") val title: String,
    @SerialName("video") val video: Boolean,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("vote_count") val voteCount: Int
)

fun MovieListResponseDTO.toDomainModel(): MovieListPageModel {
    return MovieListPageModel(
        page = page,
        totalPages = totalPages,
        movies = results.map { it.toDomainModel() }
    )
}

fun MoviePreviewDTO.toDomainModel(): MoviePreviewModel {
    return MoviePreviewModel(
        id = id,
        title = title,
        date = releaseDate.toLocalDate(),
        bannerUrl = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: ""
    )
}
