package com.product.details


import org.koin.dsl.module

val productDetailsModule = module {
    factory { ProductDetailsViewModel(get()) }
}