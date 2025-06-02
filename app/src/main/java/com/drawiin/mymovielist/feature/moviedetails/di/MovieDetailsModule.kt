package com.drawiin.mymovielist.feature.moviedetails.di

import com.drawiin.mymovielist.feature.moviedetails.data.datasource.MovieDetailsRemoteDataSource
import com.drawiin.mymovielist.feature.moviedetails.data.datasource.MovieDetailsRemoteDataSourceImpl
import com.drawiin.mymovielist.feature.moviedetails.data.repository.MovieDetailsRepository
import com.drawiin.mymovielist.feature.moviedetails.data.repository.MovieDetailsRepositoryImpl
import com.drawiin.mymovielist.feature.moviedetails.domain.usercase.GetMovieDetailsUseCase
import com.drawiin.mymovielist.feature.moviedetails.domain.usercase.GetMovieDetailsUseCaseImpl
import com.drawiin.mymovielist.feature.moviedetails.presentation.MovieDetailState
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class MovieDetailsModule {
    companion object {
        @Provides
        fun providesInitialState(): MovieDetailState  {
            return MovieDetailState.Loading
        }
    }

    @Binds
    abstract fun bindsMovieDetailsRemoteDataSource(
        impl: MovieDetailsRemoteDataSourceImpl
    ): MovieDetailsRemoteDataSource

    @Binds
    abstract fun bindsMovieDetailsRepository(
        impl: MovieDetailsRepositoryImpl
    ): MovieDetailsRepository

    @Binds
    abstract fun bindsGetMovieDetailsUseCase(
        impl: GetMovieDetailsUseCaseImpl
    ): GetMovieDetailsUseCase
}
