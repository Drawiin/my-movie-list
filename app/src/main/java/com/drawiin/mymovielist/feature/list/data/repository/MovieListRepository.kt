package com.drawiin.mymovielist.feature.list.data.repository

import com.drawiin.mymovielist.common.movie.domain.model.MoviePreviewModel

interface MovieListRepository {
    suspend fun getMovies(): Result<List<MoviePreviewModel>>
}

