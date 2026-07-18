package com.carousell.testmyapplication.network.ktor.core

interface HeaderProvider {
    fun getHeaders(): Map<String, String>
}

internal class DefaultHeaderProvider : HeaderProvider {
    override fun getHeaders(): Map<String, String> {
        return mapOf(
            "X-Platform" to "KMP",
            "Content-Type" to "application/json",
            // Add other dynamic headers here, e.g. from Settings or State
        )
    }
}
