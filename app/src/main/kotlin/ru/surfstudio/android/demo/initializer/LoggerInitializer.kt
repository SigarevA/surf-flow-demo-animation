/**
 * Copyright © 2022 Surf. All rights reserved.
 */
@file:Suppress("unused")

package ru.surfstudio.android.demo.initializer

import android.content.Context
import androidx.startup.Initializer
import ru.surfstudio.android.demo.core.logger.RemoteLogger
import ru.surfstudio.android.demo.core.logger.strategies.local.TimberLoggingStrategy
import ru.surfstudio.android.demo.core.logger.strategies.remote.FirebaseCrashlyticsRemoteLoggingStrategy
import ru.surfstudio.android.demo.core.logger.strategies.remote.RemoteLoggerLoggingStrategy
import ru.surfstudio.android.logger.Logger

/**
 * Initialization [Logger] and [RemoteLogger]
 */
class LoggerInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        Logger.addLoggingStrategy(TimberLoggingStrategy())
        Logger.addLoggingStrategy(RemoteLoggerLoggingStrategy())
        RemoteLogger.addRemoteLoggingStrategy(FirebaseCrashlyticsRemoteLoggingStrategy())
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
