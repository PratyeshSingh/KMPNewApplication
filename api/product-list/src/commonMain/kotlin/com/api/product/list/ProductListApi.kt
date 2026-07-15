package com.api.product.list

import com.api.product.list.data.ProductListResponse
import com.carousell.testmyapplication.network.ktor.core.AppNetworkClient
import com.carousell.testmyapplication.network.ktor.core.AppNetworkRequest
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines

interface ProductListApi {
    @NativeCoroutines
    suspend fun getProductList(): ProductListResponse?
}

class ProductListApiImpl(private val client: AppNetworkClient) : ProductListApi {
    companion object {
        private const val API_PATH = "/products"
    }

    override suspend fun getProductList(): ProductListResponse {
        return client.get(
            appNetworkRequest = AppNetworkRequest(
                apiPath = API_PATH
            ),
            className = ProductListResponse::class
        )
    }
}