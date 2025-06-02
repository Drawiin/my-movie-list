package com.drawiin.mymovielist.feature.moviedetails.domain.model

data class MovieDetailsModel(
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val genres: List<String>,
    val posterUrl: String,
    val backdropUrl: String,
    val voteAverage: Double,
    val voteCount: Int,
    val budget: Int,
    val revenue: Long,
    val runtime: Int,
    val status: String,
    val tagline: String
)
