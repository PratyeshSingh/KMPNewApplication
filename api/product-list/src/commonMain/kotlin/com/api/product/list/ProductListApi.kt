package com.api.product.list

import com.api.product.list.data.ProductListResponse

import com.carousell.testmyapplication.logger.logMessage
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlin.coroutines.cancellation.CancellationException

interface ProductListApi {
    @NativeCoroutines
    suspend fun getProductList(): ProductListResponse?
}

class ProductListApiImpl(private val client: HttpClient) : ProductListApi {
    companion object {
        private const val API_URL = "https://dummyjson.com/products"
    }

    override suspend fun getProductList(): ProductListResponse? {
        return try {
            client.get(API_URL).body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            logMessage("ProductListApiImpl :: ${e.stackTraceToString()}")
            null
        }
    }
}