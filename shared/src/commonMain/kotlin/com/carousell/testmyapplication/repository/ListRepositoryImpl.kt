package com.carousell.testmyapplication.repository

import com.carousell.testmyapplication.data.ProductListApi
import com.carousell.testmyapplication.data.ProductListResponse

class ListRepositoryImpl(
    private val productListApi: ProductListApi,
    // handle local storage here for offline first approach
) : ListRepository {

    override suspend fun getList(): ProductListResponse? {
        // handle local storage here for offline first approach
        return productListApi.getProductList()
    }
}