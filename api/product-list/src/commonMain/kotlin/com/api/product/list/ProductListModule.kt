package com.api.product.list

import com.api.cache.ApiCacheHolder
import com.api.cache.RealApiCacheHolder
import com.api.product.list.repository.ListRepository
import com.api.product.list.repository.ListRepositoryImpl
import com.carousell.testmyapplication.logger.logMessage
import org.koin.dsl.module

val appModule = module {
    logMessage("AppModule :: STEP1")
    single<ApiCacheHolder> { RealApiCacheHolder }
    single<ProductListApi> { ProductListApiImpl(get()) }

    single<ListRepository> { ListRepositoryImpl(get(), get()) }
    logMessage("AppModule :: STEP2")
}