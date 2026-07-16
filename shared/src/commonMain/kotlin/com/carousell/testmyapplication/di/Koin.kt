package com.carousell.testmyapplication.di

import com.api.product.list.appModule
import com.carousell.testmyapplication.network.ktor.core.di.networkModule
import com.product.details.productDetailsModule
import com.product.list.viewmodel.ListViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module


/*
 calling this functions from iOS 'iOSApp.swift' class
*/
fun initKoin() = initKoin(listOf(
    module {
        factory { ListViewModel(get()) }
    }
))

fun initKoin(extraModules: List<Module>) {
    startKoin {
        modules(
            productDetailsModule,
            networkModule,
            appModule,
            *extraModules.toTypedArray(),
        )
    }
}
