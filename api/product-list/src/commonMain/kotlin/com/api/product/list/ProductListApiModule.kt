package com.api.product.list

import com.api.cache.ApiCacheModule
import com.carousell.testmyapplication.network.ktor.core.di.NetworkModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [NetworkModule::class, ApiCacheModule::class])
@ComponentScan("com.api.product.list")
class ProductListApiModule
