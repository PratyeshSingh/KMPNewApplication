package com.foundation.preferences.di

import android.content.Context
import com.foundation.preferences.createAndroidDataStore
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.foundation.preferences")
actual class PreferencesModule {
    @Single
    fun dataStore(context: Context) = createAndroidDataStore(context)
}
