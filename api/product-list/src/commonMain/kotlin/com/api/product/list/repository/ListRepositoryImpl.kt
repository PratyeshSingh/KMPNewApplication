package com.api.product.list.repository

import com.api.product.list.ProductListApi
import com.api.product.list.data.ProductListResponse


class ListRepositoryImpl(
    private val productListApi: ProductListApi,
    // handle local storage here for offline first approach
) : ListRepository {

    override suspend fun getList(): ProductListResponse? {
        // handle local storage here for offline first approach
        return productListApi.getProductList()
    }
}