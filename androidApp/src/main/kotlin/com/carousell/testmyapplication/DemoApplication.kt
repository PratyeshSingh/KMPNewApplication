package com.carousell.testmyapplication

import android.app.Application
import com.carousell.testmyapplication.di.initKoin
import com.carousell.testmyapplication.viewmodel.ListViewModel
import org.koin.dsl.module

class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin(
            listOf(
                module {
                    factory { ListViewModel(get()) }
                }
            )
        )

    }
}
