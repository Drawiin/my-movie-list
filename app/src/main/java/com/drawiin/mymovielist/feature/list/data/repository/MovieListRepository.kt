package com.drawiin.mymovielist.feature.list.data.repository

import com.drawiin.mymovielist.feature.list.domain.model.MovieListPageModel
import com.drawiin.mymovielist.feature.list.domain.model.MoviePreviewModel

interface MovieListRepository {
    suspend fun getMovies(page: Int): Result<MovieListPageModel>
}

