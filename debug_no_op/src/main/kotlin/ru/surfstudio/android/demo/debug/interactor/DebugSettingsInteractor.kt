/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.debug.interactor

import android.content.Context
import ru.surfstudio.android.demo.domain.ServerType

@Suppress("UNUSED_PARAMETER")
class DebugSettingsInteractor(private val context: Context) {

    /** Get app server type */
    fun getServerTypeBlocking(): ServerType = ServerType.PROD

    /** Set app server type */
    suspend fun setServerType(value: ServerType) {}
}