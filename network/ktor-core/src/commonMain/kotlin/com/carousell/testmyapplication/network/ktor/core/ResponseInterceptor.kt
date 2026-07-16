package com.carousell.testmyapplication.network.ktor.core

import com.carousell.testmyapplication.logger.logMessage
import io.ktor.client.HttpClientConfig
import io.ktor.client.statement.HttpReceivePipeline

fun HttpClientConfig<*>.installResponseInterceptor() {
    install("ResponseInterceptor") {
        receivePipeline.intercept(HttpReceivePipeline.After) { response ->
            if (response.status.value == 400) {
                // handle you state here
            }

            logMessage("ResponseInterceptor:: INSIDE")
            proceedWith(response)
        }
    }
}