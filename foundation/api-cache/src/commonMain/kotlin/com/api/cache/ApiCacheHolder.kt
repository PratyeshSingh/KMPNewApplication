package com.api.cache

object ApiCacheHolder {
    private val apiMapHolders: HashMap<String, Any> = HashMap()

    fun saveApiDetails(key: String, values: Any) {
        apiMapHolders[key] = values
    }

    fun fetchApiDetails(key: String): Any? {
        return apiMapHolders[key]
    }
}