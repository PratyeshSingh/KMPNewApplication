package com.carousell.testmyapplication.network.ktor.core

import io.ktor.client.request.header

interface HeaderProvider {
    fun getHeaders(): Map<String, String>
}

class DefaultHeaderProvider : HeaderProvider {
    override fun getHeaders(): Map<String, String> {
        return mapOf(
            "X-Platform" to "KMP",
            "Content-Type" to "application/json",
            // Add other dynamic headers here, e.g. from Settings or State
        )
    }
}
