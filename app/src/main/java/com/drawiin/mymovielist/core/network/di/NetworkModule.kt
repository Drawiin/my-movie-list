package com.drawiin.mymovielist.core.network.di

import com.drawiin.mymovielist.BuildConfig
import com.drawiin.mymovielist.core.network.client.MyMovieListNetworkClient
import com.drawiin.mymovielist.core.network.client.MyMovieListNetworkClientImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {
    companion object {
        @Provides
        fun providesHttpClient(): HttpClient {
            return HttpClient(Android) {
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.ALL
                }

                install(ContentNegotiation) {
                    json(
                        Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                        }
                    )
                }

                defaultRequest {
                    url("https://api.themoviedb.org/3/movie/")
                    header(HttpHeaders.Authorization, "Bearer ${BuildConfig.TMDB_API_TOKEN}")
                }
            }
        }
    }

    @Binds
    abstract fun bindsNetworkClient(
        client: MyMovieListNetworkClientImpl
    ): MyMovieListNetworkClient
}
