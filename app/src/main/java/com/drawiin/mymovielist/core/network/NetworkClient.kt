package com.drawiin.mymovielist.core.network

import com.drawiin.mymovielist.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.reflect.TypeInfo
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

interface NetworkClient {
    suspend fun <T : Any> get(
        url: String,
        clazz: KClass<T>,
        block: HttpRequestBuilder.() -> Unit = {}
    ): Result<T>
}

class NetworkClientImpl(
    // TODO this trough DI
    private val httpClient: HttpClient = HttpClient(Android) {
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
    }
) : NetworkClient {
    override suspend fun <T : Any> get(
        url: String,
        clazz: KClass<T>,
        block: HttpRequestBuilder.() -> Unit
    ): Result<T> {
        return runCatching {
            httpClient.get(url, addHeaders(block)).body(typeInfo = TypeInfo(clazz, null))
        }
    }

    private fun addHeaders(block: HttpRequestBuilder.() -> Unit): HttpRequestBuilder.() -> Unit {
        return {
            headers {
                append(
                    "Authorization",
                    "Bearer ${BuildConfig.TMDB_API_TOKEN}"
                )
            }
            block(this)
        }
    }
}
