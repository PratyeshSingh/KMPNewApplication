package com.carousell.testmyapplication.network.ktor.core

import com.carousell.testmyapplication.logger.logMessage
import io.ktor.client.HttpClientConfig
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.header
import kotlin.time.Clock

fun HttpClientConfig<*>.installRequestInterceptor() {
    install("RequestInterceptor") {
        requestPipeline.intercept(HttpRequestPipeline.State) { response ->
            // Modify request here
            context.header("X-Timestamp", Clock.System.now().toEpochMilliseconds().toString())

            logMessage("RequestInterceptor:: INSIDE")
            proceed()
        }
    }
}
