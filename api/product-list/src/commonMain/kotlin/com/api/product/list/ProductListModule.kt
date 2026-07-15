package com.api.product.list

import com.api.product.list.repository.ListRepository
import com.api.product.list.repository.ListRepositoryImpl
import com.carousell.testmyapplication.logger.logMessage
import org.koin.dsl.module

val appModule = module {
    logMessage("AppModule :: STEP1")
    single<ProductListApi> { ProductListApiImpl(get()) }

    single<ListRepository> { ListRepositoryImpl(get()) }

    logMessage("AppModule :: STEP2")
}