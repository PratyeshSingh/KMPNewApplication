package com.carousell.testmyapplication.di

import com.carousell.testmyapplication.data.ProductListApi
import com.carousell.testmyapplication.data.ProductListApiImpl
import com.carousell.testmyapplication.repository.ListRepository
import com.carousell.testmyapplication.repository.ListRepositoryImpl
import com.carousell.testmyapplication.util.logMessage
//import com.carousell.testmyapplication.viewmodel.ListViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    logMessage("networkModule :: STEP1")
    single<HttpClient> {
        val json = Json { ignoreUnknownKeys = true }
        HttpClient {
            install(ContentNegotiation) {
                // TODO Fix API so it serves application/json
                json(json, contentType = ContentType.Any)
            }
        }
    }
    single<ProductListApi> { ProductListApiImpl(get()) }

    single<ListRepository> { ListRepositoryImpl(get()) }

    logMessage("networkModule :: STEP2")
}