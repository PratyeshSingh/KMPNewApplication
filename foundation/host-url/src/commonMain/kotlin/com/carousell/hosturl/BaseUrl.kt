package com.carousell.hosturl

object BaseUrl {
    private const val API_HOST_PROD = "https://prod.dummyjson.com"
    private const val API_HOST_DEBUG = "https://dummyjson.com"

    fun getHostURL(debug: Boolean = true): String {
        return if (debug) {
            API_HOST_DEBUG
        } else {
            API_HOST_PROD
        }
    }
}
