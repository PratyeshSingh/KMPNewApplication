package com.carousell.testmyapplication

import android.app.Application
import leakcanary.LeakCanary

object LeakCanaryInitializer {
    fun init(application: Application) {
        LeakCanary.config = LeakCanary.config.copy(
            // Max number of heap dumps to keep on disk. 
            // Reducing this saves internal storage and overhead.
            maxStoredHeapDumps = 2,
            // Number of retained objects before triggering a heap dump while the app is visible.
            // Increasing this prevents frequent OOM-triggering heap dumps.
            retainedVisibleThreshold = 10,
            // If true, LeakCanary will compute the retained heap size. 
            // Disabling this saves significant memory and CPU during analysis.
            computeRetainedHeapSize = false
        )
    }
}
