package com.drawiin.mymovielist.common.movie.domain.model

data class MovieModel(
    val id: Int,
    val isInWatchList: Boolean,
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
