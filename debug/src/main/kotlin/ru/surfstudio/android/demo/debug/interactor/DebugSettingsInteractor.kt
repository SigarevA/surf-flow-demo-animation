/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.debug.interactor

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ru.surfstudio.android.demo.domain.ServerType

private val Context.debugSettingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "debug_settings")
private val serverTypeKey = intPreferencesKey("server_type")

/** Interactor for app debug settings */
class DebugSettingsInteractor(private val context: Context) {

    /** Get app server type */
    // todo clear cache on server changed
    fun getServerTypeBlocking(): ServerType =
        runBlocking {
            context.debugSettingsDataStore.data.first().let {
                it[serverTypeKey]?.let { value ->
                    ServerType.values()[value]
                } ?: ServerType.PROD
            }
        }

    /** Set app server type */
    suspend fun setServerType(value: ServerType) {
        withContext(Dispatchers.IO) {
            context.debugSettingsDataStore.edit { it[serverTypeKey] = value.ordinal }
        }
    }
}