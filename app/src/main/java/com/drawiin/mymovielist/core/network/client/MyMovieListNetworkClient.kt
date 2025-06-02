package com.drawiin.mymovielist.core.network.client

import io.ktor.client.request.HttpRequestBuilder
import kotlin.reflect.KClass

interface MyMovieListNetworkClient {
    suspend fun <T : Any> get(
        url: String,
        clazz: KClass<T>,
        block: HttpRequestBuilder.() -> Unit = {}
    ): Result<T>
}

