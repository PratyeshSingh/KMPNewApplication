package com.carousell.testmyapplication

import android.app.Application

object LeakCanaryInitializer {
    fun init(application: Application) {
        // No-op for release builds
    }
}
