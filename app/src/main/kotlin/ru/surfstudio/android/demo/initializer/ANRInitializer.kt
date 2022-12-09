/**
 * Copyright © 2022 Surf. All rights reserved.
 */
@file:Suppress("unused")

package ru.surfstudio.android.demo.initializer

import android.content.Context
import androidx.startup.Initializer
import com.github.anrwatchdog.ANRWatchDog
import ru.surfstudio.android.demo.core.logger.RemoteLogger

/**
 * Initialization [ANRWatchDog]
 */
class ANRInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        ANRWatchDog().setReportMainThreadOnly()
            .setANRListener { RemoteLogger.logError(it) }
            .start()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
