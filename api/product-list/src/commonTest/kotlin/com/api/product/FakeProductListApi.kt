package com.api.product

import com.api.cache.ApiCacheHolder
import com.api.product.list.ProductListApi
import com.api.product.list.data.ProductListResponse

class FakeProductListApi : ProductListApi {
    var responseToReturn: ProductListResponse? = null
    var shouldSearchThrowException = false
    var getProductListCalled = false
    var searchProductCalled = false
    var searchQueryPassed: String? = null

    override suspend fun getProductList(): ProductListResponse? {
        getProductListCalled = true
        return responseToReturn
    }

    override suspend fun searchProduct(searchQuery: String): ProductListResponse? {
        searchProductCalled = true
        searchQueryPassed = searchQuery
        return responseToReturn
    }
}

class FakeApiCacheHolder : ApiCacheHolder {
    var savedKey: String? = null
    var savedResponse: Any? = null
    var saveCalled = false

    override fun saveApiDetails(key: String, data: Any) {
        saveCalled = true
        savedKey = key
        savedResponse = data
    }

    override fun fetchApiDetails(key: String): Any? {
        TODO("Not yet implemented")
    }
}