package com.drawiin.mymovielist.feature.list.di

import com.drawiin.mymovielist.feature.list.data.datasource.remote.MovieListRemoteDataSource
import com.drawiin.mymovielist.feature.list.data.datasource.remote.MovieListRemoteDataSourceImpl
import com.drawiin.mymovielist.feature.list.data.repository.MovieListRepository
import com.drawiin.mymovielist.feature.list.data.repository.MovieListRepositoryImpl
import com.drawiin.mymovielist.feature.list.domain.usecase.GetMovieListUseCase
import com.drawiin.mymovielist.feature.list.domain.usecase.GetMovieListUseCaseImpl
import com.drawiin.mymovielist.feature.list.presentation.MovieListState
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class MovieListModule {
    companion object {
        @Provides
        fun providesInitialState(): MovieListState {
            return MovieListState()
        }
    }

    @Binds
    abstract fun bindsMovieListRemoteDataSource(
        impl: MovieListRemoteDataSourceImpl
    ): MovieListRemoteDataSource

    @Binds
    abstract fun bindsMovieListRepository(
        impl: MovieListRepositoryImpl
    ): MovieListRepository

    @Binds
    abstract fun bindsGetMovieListUseCase(
        impl: GetMovieListUseCaseImpl
    ): GetMovieListUseCase
}
