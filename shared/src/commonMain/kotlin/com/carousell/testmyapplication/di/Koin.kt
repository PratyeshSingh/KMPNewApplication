package com.carousell.testmyapplication.di

import com.api.cache.ApiCacheModule
import com.api.product.list.ProductListApiModule
import com.carousell.testmyapplication.network.ktor.core.di.NetworkModule
import com.product.details.ProductDetailsModule
import com.product.list.ProductListModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.core.module.Module as KoinModule
import org.koin.plugin.module.dsl.startKoin

@Module(includes = [
    ProductListApiModule::class,
    ProductDetailsModule::class,
    ProductListModule::class,
    NetworkModule::class,
    ApiCacheModule::class
])
@ComponentScan("com.carousell.testmyapplication.di")
class SharedModule

@KoinApplication(modules = [SharedModule::class])
class SharedApp

/*
 calling this function from iOS 'iOSApp.swift' class
*/
fun initKoin() = initKoin(emptyList())

fun initKoin(extraModules: List<KoinModule>) {
    startKoin<SharedApp> {
        modules(*extraModules.toTypedArray())
    }
}
