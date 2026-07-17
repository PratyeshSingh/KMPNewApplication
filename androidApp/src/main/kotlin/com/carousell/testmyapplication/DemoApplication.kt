package com.carousell.testmyapplication

import android.app.Application
import com.carousell.testmyapplication.di.initKoin
import com.product.list.viewmodel.ListViewModel
import org.koin.dsl.module

class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin(
            listOf(
                module {
//                    initKoin now automatically includes ListViewModel via SharedApp / ProductListModule
//                    factory { ListViewModel(get()) }
                }
            )
        )
    }
}
