package com.carousell.testmyapplication.network.ktor.core

import com.foundation.preferences.AppDataStore

interface HeaderProvider {
    suspend fun getHeaders(): Map<String, String>
}

internal class DefaultHeaderProvider(
    private val appDataStore: AppDataStore,
) : HeaderProvider {
    override suspend fun getHeaders(): Map<String, String> {
        val headers =
            mutableMapOf(
                "X-Platform" to "KMP",
                "Content-Type" to "application/json",
            )

        appDataStore.getData(AppDataStore.ACCESS_TOKEN)?.let {
            headers["Authorization"] = "Bearer $it"
        }

        return headers
    }
}
