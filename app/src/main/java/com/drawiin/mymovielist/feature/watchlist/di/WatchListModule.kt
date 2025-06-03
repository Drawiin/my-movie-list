package com.drawiin.mymovielist.feature.watchlist.di

import com.drawiin.mymovielist.feature.watchlist.presentation.WatchListState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class WatchListModule {
    companion object {
        @Provides
        fun provideInitialState(): WatchListState {
            return WatchListState.Loading
        }
    }
}
