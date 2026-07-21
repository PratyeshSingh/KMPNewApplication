package com.carousell.testmyapplication

import android.app.Application
import com.carousell.testmyapplication.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@DemoApplication)
            androidLogger()
        }
    }
}
