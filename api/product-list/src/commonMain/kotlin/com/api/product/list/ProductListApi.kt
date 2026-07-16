package com.api.product.list

import com.api.product.list.data.ProductListResponse
import com.carousell.testmyapplication.network.ktor.core.AppNetworkClient
import com.carousell.testmyapplication.network.ktor.core.AppNetworkRequest
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines

interface ProductListApi {
    @NativeCoroutines
    suspend fun getProductList(): ProductListResponse?

    @NativeCoroutines
    suspend fun searchProduct(searchQuery: String): ProductListResponse?
}

class ProductListApiImpl(private val client: AppNetworkClient) : ProductListApi {
    companion object {
        private const val API_PATH_PRODUCT_LIST = "/products"
        private const val API_PATH_SEARCH_PRODUCT = "/products/search?q="
    }

    override suspend fun getProductList(): ProductListResponse {
        return client.get(
            appNetworkRequest = AppNetworkRequest(
                apiPath = API_PATH_PRODUCT_LIST
            ),
            className = ProductListResponse::class
        )
    }

    override suspend fun searchProduct(searchQuery: String): ProductListResponse {
        return client.get(
            appNetworkRequest = AppNetworkRequest(
                apiPath = API_PATH_SEARCH_PRODUCT.plus(searchQuery)
            ),
            className = ProductListResponse::class
        )
    }
}