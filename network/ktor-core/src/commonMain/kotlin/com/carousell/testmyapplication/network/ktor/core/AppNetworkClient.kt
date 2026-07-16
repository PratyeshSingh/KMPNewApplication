package com.carousell.testmyapplication.network.ktor.core

import com.carousell.hosturl.BaseUrl
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
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

        val response = httpClient.get(urlString) {
            applyRequestParams(appNetworkRequest)
        }

        return response.body(TypeInfo(type = className, null))
    }

    suspend fun <T : Any> post(
        appNetworkRequest: AppNetworkRequest,
        className: KClass<T>
    ): T {
        val urlString = BaseUrl.getHostURL().plus(appNetworkRequest.apiPath)

        val response = httpClient.post(urlString) {
            applyRequestParams(appNetworkRequest)
            setBody(appNetworkRequest.requestBody)
        }

        return response.body(TypeInfo(type = className, null))
    }

    suspend fun <T : Any> put(
        appNetworkRequest: AppNetworkRequest,
        className: KClass<T>
    ): T {
        val urlString = BaseUrl.getHostURL().plus(appNetworkRequest.apiPath)

        val response = httpClient.put(urlString) {
            applyRequestParams(appNetworkRequest)
            setBody(appNetworkRequest.requestBody)
        }

        return response.body(TypeInfo(type = className, null))
    }

    suspend fun <T : Any> delete(
        appNetworkRequest: AppNetworkRequest,
        className: KClass<T>
    ): T {
        val urlString = BaseUrl.getHostURL().plus(appNetworkRequest.apiPath)

        val response = httpClient.delete(urlString) {
            applyRequestParams(appNetworkRequest)
        }

        return response.body(TypeInfo(type = className, null))
    }

    suspend fun <T : Any> patch(
        appNetworkRequest: AppNetworkRequest,
        className: KClass<T>
    ): T {
        val urlString = BaseUrl.getHostURL().plus(appNetworkRequest.apiPath)

        val response = httpClient.patch(urlString) {
            applyRequestParams(appNetworkRequest)
            setBody(appNetworkRequest.requestBody)
        }

        return response.body(TypeInfo(type = className, null))
    }

    private fun HttpRequestBuilder.applyRequestParams(appNetworkRequest: AppNetworkRequest) {
        // 1. Dynamic headers for this specific request
        appNetworkRequest.headers.forEach { (key, value) ->
            header(key, value)
        }

        // 2. Query parameters
        appNetworkRequest.requestQueryParam.forEach { (key, value) ->
            url.parameters.append(key, value)
        }
    }
}