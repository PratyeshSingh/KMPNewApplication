package com.api.product.list.repository

import com.api.cache.ApiCacheHolder
import com.api.product.list.ProductListApi
import com.api.product.list.data.ProductListResponse


class ListRepositoryImpl(
    private val productListApi: ProductListApi,
    private val apiCacheHolder: ApiCacheHolder
) : ListRepository {

    override suspend fun getList(): ProductListResponse? {
        // handle local storage here for offline first approach
        val response = productListApi.getProductList()
        response?.let {
            apiCacheHolder.saveApiDetails("PRODUCTLIST", response)
        }
        return response
    }
}