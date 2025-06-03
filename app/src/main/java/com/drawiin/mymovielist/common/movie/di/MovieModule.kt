package com.drawiin.mymovielist.common.movie.di

import android.content.Context
import androidx.room.Room
import com.drawiin.mymovielist.common.movie.data.datasource.local.MovieDao
import com.drawiin.mymovielist.common.movie.data.datasource.local.MovieDataBase
import com.drawiin.mymovielist.common.movie.data.repository.MovieRepository
import com.drawiin.mymovielist.common.movie.data.repository.MovieRepositoryImpl
import com.drawiin.mymovielist.common.movie.domain.usecase.AddMovieToWatchListUseCase
import com.drawiin.mymovielist.common.movie.domain.usecase.AddMovieToWatchListUseCaseImpl
import com.drawiin.mymovielist.common.movie.domain.usecase.CheckMovieInWatchListUseCase
import com.drawiin.mymovielist.common.movie.domain.usecase.CheckMovieInWatchListUseCaseImpl
import com.drawiin.mymovielist.common.movie.domain.usecase.GetWatchListUseCase
import com.drawiin.mymovielist.common.movie.domain.usecase.GetWatchListUseCaseImpl
import com.drawiin.mymovielist.common.movie.domain.usecase.RemoveMovieFromWatchListUseCase
import com.drawiin.mymovielist.common.movie.domain.usecase.RemoveMovieFromWatchListUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MovieModule {
    companion object {
        @Provides
        fun provideMovieDatabase(@ApplicationContext context: Context): MovieDataBase =
            Room.databaseBuilder(
                context,
                MovieDataBase::class.java,
                "movie_db"
            ).build()

        @Provides
        fun provideMovieDao(movieDataBase: MovieDataBase): MovieDao = movieDataBase.movieDao()
    }

    @Binds
    abstract fun bindsMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    abstract fun bindsAddMovieToWatchListUseCase(
        impl: AddMovieToWatchListUseCaseImpl
    ): AddMovieToWatchListUseCase

    @Binds
    abstract fun bindsCheckMovieInWatchListUseCase(
        impl: CheckMovieInWatchListUseCaseImpl
    ): CheckMovieInWatchListUseCase

    @Binds
    abstract fun bindsRemoveMovieFromWatchListUseCase(
        impl: RemoveMovieFromWatchListUseCaseImpl
    ): RemoveMovieFromWatchListUseCase

    @Binds
    abstract fun bindsGetWatchListUseCase(
        impl: GetWatchListUseCaseImpl
    ): GetWatchListUseCase
}
