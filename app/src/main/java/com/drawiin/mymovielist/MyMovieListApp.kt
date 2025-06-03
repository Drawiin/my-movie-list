package com.drawiin.mymovielist

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import com.drawiin.mymovielist.core.navigation.AppRoutes
import com.drawiin.mymovielist.core.uikit.foundation.MyMovieListTheme
import com.drawiin.mymovielist.feature.list.presentation.MovieListScreen
import com.drawiin.mymovielist.feature.moviedetails.presentation.MovieDetailScreen
import com.drawiin.mymovielist.feature.watchlist.presentation.WatchListScreen

@Composable
fun MyMovieListApp() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }
    MyMovieListTheme {
        MyMovieListAppNavGraph()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyMovieListAppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppRoutes.MovieListingRote) {
        composable<AppRoutes.MovieListingRote> {
            MovieListScreen(
                viewModel = hiltViewModel(),
                onGoToMovieDetails = {
                    navController.navigate(AppRoutes.MovieDetailsRoute(it))
                },
                onGoToWatchList = {
                    navController.navigate(AppRoutes.WatchListRoute)
                }
            )
        }

        composable<AppRoutes.MovieDetailsRoute> { backStackEntry ->
            MovieDetailScreen(
                viewModel = hiltViewModel(),
                movieId = backStackEntry.toRoute<AppRoutes.MovieDetailsRoute>().movieId,
                onGoBack = {
                    navController.popBackStack()
                }
            )

        }

        composable<AppRoutes.WatchListRoute> {
            WatchListScreen(
                viewModel = hiltViewModel(),
                onGoBack = {
                    navController.popBackStack()
                },
                onGoToDetails = { movieId ->
                    navController.navigate(AppRoutes.MovieDetailsRoute(movieId))
                }
            )
        }
    }
}
