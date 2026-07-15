package com.carousell.testmyapplication.di

import com.carousell.testmyapplication.data.ProductListApi
import com.carousell.testmyapplication.data.ProductListApiImpl
import com.carousell.testmyapplication.repository.ListRepository
import com.carousell.testmyapplication.repository.ListRepositoryImpl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single {
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
}