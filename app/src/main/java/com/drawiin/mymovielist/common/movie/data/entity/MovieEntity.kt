package com.drawiin.mymovielist.common.movie.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.drawiin.mymovielist.common.movie.domain.model.MovieModel

@Entity(tableName = "movie")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val genres: String, // Store as comma-separated string
    val posterUrl: String,
    val backdropUrl: String,
    val voteAverage: Double,
    val voteCount: Int,
    val budget: Int,
    val revenue: Long,
    val runtime: Int,
    val status: String,
    val tagline: String
) {
    companion object {
        fun fromModel(model: MovieModel): MovieEntity {
            return MovieEntity(
                id = model.id,
                title = model.title,
                overview = model.overview,
                releaseDate = model.releaseDate,
                genres = model.genres.joinToString(","),
                posterUrl = model.posterUrl,
                backdropUrl = model.backdropUrl,
                voteAverage = model.voteAverage,
                voteCount = model.voteCount,
                budget = model.budget,
                revenue = model.revenue,
                runtime = model.runtime,
                status = model.status,
                tagline = model.tagline
            )
        }
    }
}

fun MovieEntity.toDomainModel(): MovieModel {
    return MovieModel(
        id = id,
        title = title,
        overview = overview,
        releaseDate = releaseDate,
        genres = genres.split(","),
        posterUrl = posterUrl,
        backdropUrl = backdropUrl,
        voteAverage = voteAverage,
        voteCount = voteCount,
        budget = budget,
        revenue = revenue,
        runtime = runtime,
        status = status,
        tagline = tagline,
        isInWatchList = true
    )
}
