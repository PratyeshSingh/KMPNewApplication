package com.foundation.preferences.di

import com.foundation.preferences.createIosDataStore
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.foundation.preferences")
actual class PreferencesModule {
    @Single
    fun dataStore() = createIosDataStore()
}
