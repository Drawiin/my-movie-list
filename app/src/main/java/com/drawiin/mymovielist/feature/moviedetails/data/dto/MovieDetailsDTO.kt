package com.drawiin.mymovielist.feature.moviedetails.data.dto

import com.drawiin.mymovielist.core.localization.toLocalDate
import com.drawiin.mymovielist.common.movie.domain.model.MovieModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsDTO(
    @SerialName("adult") val adultDTO: Boolean,
    @SerialName("backdrop_path") val backdropPathDTO: String?,
    @SerialName("budget") val budgetDTO: Int,
    @SerialName("genres") val genresDTO: List<GenreDTO>,
    @SerialName("homepage") val homepageDTO: String?,
    @SerialName("id") val idDTO: Int,
    @SerialName("imdb_id") val imdbIdDTO: String?,
    @SerialName("origin_country") val originCountryDTO: List<String>,
    @SerialName("original_language") val originalLanguageDTO: String,
    @SerialName("original_title") val originalTitleDTO: String,
    @SerialName("overview") val overviewDTO: String?,
    @SerialName("popularity") val popularityDTO: Double,
    @SerialName("poster_path") val posterPathDTO: String?,
    @SerialName("production_companies") val productionCompaniesDTO: List<ProductionCompanyDTO>,
    @SerialName("production_countries") val productionCountriesDTO: List<ProductionCountryDTO>,
    @SerialName("release_date") val releaseDateDTO: String,
    @SerialName("revenue") val revenueDTO: Long,
    @SerialName("runtime") val runtimeDTO: Int?,
    @SerialName("status") val statusDTO: String,
    @SerialName("tagline") val taglineDTO: String?,
    @SerialName("title") val titleDTO: String,
    @SerialName("video") val videoDTO: Boolean,
    @SerialName("vote_average") val voteAverageDTO: Double,
    @SerialName("vote_count") val voteCountDTO: Int
)

@Serializable
data class GenreDTO(
    @SerialName("id") val idDTO: Int,
    @SerialName("name") val nameDTO: String
)

@Serializable
data class ProductionCompanyDTO(
    @SerialName("id") val idDTO: Int,
    @SerialName("logo_path") val logoPathDTO: String?,
    @SerialName("name") val nameDTO: String,
    @SerialName("origin_country") val originCountryDTO: String
)

@Serializable
data class ProductionCountryDTO(
    @SerialName("iso_3166_1") val iso3166_1DTO: String,
    @SerialName("name") val nameDTO: String
)

fun MovieDetailsDTO.toDomainModel(): MovieModel {
    return MovieModel(
        id = idDTO,
        title = titleDTO,
        overview = overviewDTO ?: "",
        releaseDate = releaseDateDTO.toLocalDate(),
        genres = genresDTO.map { it.nameDTO },
        posterUrl = posterPathDTO?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        backdropUrl = backdropPathDTO?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        voteAverage = voteAverageDTO,
        voteCount = voteCountDTO,
        budget = budgetDTO,
        revenue = revenueDTO,
        runtime = runtimeDTO ?: 0,
        status = statusDTO,
        tagline = taglineDTO ?: "",
        isInWatchList = false
    )
}
