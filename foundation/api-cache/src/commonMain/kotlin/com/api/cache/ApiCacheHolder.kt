package com.api.cache

interface ApiCacheHolder {
    suspend fun saveApiDetails(key: String, data: Any)

    suspend fun fetchApiDetails(key: String): Any?
}

object RealApiCacheHolder : ApiCacheHolder {
    private val apiMapHolders: HashMap<String, Any> = HashMap()

    override suspend fun saveApiDetails(key: String, data: Any) {
        apiMapHolders[key] = data
    }

    override suspend fun fetchApiDetails(key: String): Any? {
        return apiMapHolders[key]
    }
}