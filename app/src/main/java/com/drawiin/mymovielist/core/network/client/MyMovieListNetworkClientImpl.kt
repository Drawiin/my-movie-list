package com.drawiin.mymovielist.core.network.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.util.reflect.TypeInfo
import javax.inject.Inject
import kotlin.reflect.KClass

class MyMovieListNetworkClientImpl @Inject constructor(
    private val httpClient: HttpClient
) : MyMovieListNetworkClient {
    override suspend fun <T : Any> get(
        url: String,
        clazz: KClass<T>,
        block: HttpRequestBuilder.() -> Unit
    ): Result<T> {
        return runCatching {
            httpClient.get(url, block).body(typeInfo = TypeInfo(clazz, null))
        }
    }
}
