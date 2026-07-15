package com.carousell.testmyapplication.network.ktor.core

import com.carousell.hosturl.BaseUrl
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.util.reflect.TypeInfo
import kotlin.reflect.KClass

class AppNetworkClient(
    private val httpClient: HttpClient,
) {
    suspend fun <T : Any> get(
        appNetworkRequest: AppNetworkRequest,
        className: KClass<T>
    ): T {
        val urlString = BaseUrl.getHostURL().plus(appNetworkRequest.apiPath)

        val response = httpClient.get(urlString)

        // We construct a TypeInfo so Ktor's engine knows the runtime class type
        val typeInfo = TypeInfo(type = className, null)

        // We pass the typeInfo and serializer directly to Ktor
        return response.body(typeInfo)
    }

    suspend fun <T : Any> post(
        appNetworkRequest: AppNetworkRequest,
        className: KClass<T>
    ): T {
        val urlString = BaseUrl.getHostURL().plus(appNetworkRequest.apiPath)

        val response = httpClient.post(urlString)

        // We construct a TypeInfo so Ktor's engine knows the runtime class type
        val typeInfo = TypeInfo(type = className, null)

        // We pass the typeInfo and serializer directly to Ktor
        return response.body(typeInfo)
    }

    suspend fun <T : Any> put(
        appNetworkRequest: AppNetworkRequest,
        className: KClass<T>
    ): T {
        val urlString = BaseUrl.getHostURL().plus(appNetworkRequest.apiPath)

        val response = httpClient.put(urlString)

        // We construct a TypeInfo so Ktor's engine knows the runtime class type
        val typeInfo = TypeInfo(type = className, null)

        // We pass the typeInfo and serializer directly to Ktor
        return response.body(typeInfo)
    }

    suspend fun <T : Any> delete(
        appNetworkRequest: AppNetworkRequest,
        className: KClass<T>
    ): T {
        val urlString = BaseUrl.getHostURL().plus(appNetworkRequest.apiPath)

        val response = httpClient.delete(urlString)

        // We construct a TypeInfo so Ktor's engine knows the runtime class type
        val typeInfo = TypeInfo(type = className, null)

        // We pass the typeInfo and serializer directly to Ktor
        return response.body(typeInfo)
    }

    suspend fun <T : Any> patch(
        appNetworkRequest: AppNetworkRequest,
        className: KClass<T>
    ): T {
        val urlString = BaseUrl.getHostURL().plus(appNetworkRequest.apiPath)

        val response = httpClient.patch(urlString)

        // We construct a TypeInfo so Ktor's engine knows the runtime class type
        val typeInfo = TypeInfo(type = className, null)

        // We pass the typeInfo and serializer directly to Ktor
        return response.body(typeInfo)
    }
}