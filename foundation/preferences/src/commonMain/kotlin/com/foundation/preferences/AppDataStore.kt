package com.foundation.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

interface AppDataStore {
    companion object {
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
    }

    suspend fun updateData(
        key: String,
        value: String,
    )

    suspend fun getData(key: String): String?
}

@Single
class DefaultAppDataStore(
    private val dataStore: DataStore<Preferences>,
) : AppDataStore {
    override suspend fun updateData(
        key: String,
        value: String,
    ) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    override suspend fun getData(key: String): String? {
        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(key)]
        }.first()
    }
}
