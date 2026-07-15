package com.carousell.testmyapplication.network.ktor.core.di

import com.carousell.testmyapplication.logger.logMessage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    logMessage("NetworkModule :: STEP1")
    single<HttpClient> {
        val json = Json { ignoreUnknownKeys = true }
        HttpClient {
            install(ContentNegotiation) {
                // TODO Fix API so it serves application/json
                json(json, contentType = ContentType.Any)
            }
        }
    }

    logMessage("NetworkModule :: STEP2")
}