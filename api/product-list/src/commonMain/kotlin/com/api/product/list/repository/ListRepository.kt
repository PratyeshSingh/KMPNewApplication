package com.api.product.list.repository

import com.api.product.list.data.ProductListResponse
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines


interface ListRepository {
    @NativeCoroutines
    suspend fun getList(): ProductListResponse?
    @NativeCoroutines
    suspend fun searchProduct(searchQuery: String): ProductListResponse?
}