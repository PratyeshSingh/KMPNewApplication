package com.product.details

import com.api.cache.ApiCacheModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [ApiCacheModule::class])
@ComponentScan("com.product.details")
class ProductDetailsModule
