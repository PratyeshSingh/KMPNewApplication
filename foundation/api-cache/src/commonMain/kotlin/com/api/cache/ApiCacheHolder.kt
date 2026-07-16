package com.api.cache

interface ApiCacheHolder {
    fun saveApiDetails(key: String, data: Any)

    fun fetchApiDetails(key: String): Any?
}

object RealApiCacheHolder : ApiCacheHolder {
    private val apiMapHolders: HashMap<String, Any> = HashMap()

    override fun saveApiDetails(key: String, values: Any) {
        apiMapHolders[key] = values
    }

    override fun fetchApiDetails(key: String): Any? {
        return apiMapHolders[key]
    }
}