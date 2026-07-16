package com.api.product.list.repository

import com.api.product.list.data.ProductListResponse


interface ListRepository {
    suspend fun getList(): ProductListResponse?
    suspend fun searchProduct(searchQuery: String): ProductListResponse?
}