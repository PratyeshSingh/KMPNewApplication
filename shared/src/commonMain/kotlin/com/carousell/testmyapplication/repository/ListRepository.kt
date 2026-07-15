package com.carousell.testmyapplication.repository

import com.carousell.testmyapplication.data.ProductListResponse

interface ListRepository {
    suspend fun getList(): ProductListResponse?
}