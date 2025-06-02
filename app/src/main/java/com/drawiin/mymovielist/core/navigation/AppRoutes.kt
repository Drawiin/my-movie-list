package com.drawiin.mymovielist.core.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoutes {
    @Serializable
    object MovieListingRote : AppRoutes

    @Serializable
    data class MovieDetailsRoute(
        @SerialName("movieId")
        val movieId: Int
    ) : AppRoutes

}

