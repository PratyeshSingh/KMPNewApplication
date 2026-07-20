package com.carousell.testmyapplication.di

import com.api.cache.ApiCacheModule
import com.api.product.list.ProductListApiModule
import com.carousell.testmyapplication.network.ktor.core.di.NetworkModule
import com.foundation.preferences.di.PreferencesModule
import com.product.details.ProductDetailsModule
import com.product.list.ProductListModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.plugin.module.dsl.startKoin

@Module(
    includes = [
        PreferencesModule::class,
        ProductListApiModule::class,
        ProductDetailsModule::class,
        ProductListModule::class,
        NetworkModule::class,
        ApiCacheModule::class,
    ],
)
@ComponentScan("com.carousell.testmyapplication.di")
class SharedModule

@KoinApplication(modules = [SharedModule::class])
class SharedApp

/*
 calling this function from iOS 'iOSApp.swift' class
*/
fun initKoin() = initKoin {}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin<SharedApp> {
        appDeclaration()
    }
}
