package com.product.list

import com.api.product.list.ProductListApiModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [ProductListApiModule::class])
@ComponentScan("com.product.list")
class ProductListModule
