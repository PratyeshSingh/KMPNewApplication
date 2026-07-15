package com.carousell.testmyapplication.network.ktor.core

data class AppNetworkRequest (
    val headers: Map<String, String> = emptyMap(),
    val requestBody: Map<String, String> = emptyMap(),
    val requestQueryParam: Map<String, String> = emptyMap(),
    val apiPath: String
)