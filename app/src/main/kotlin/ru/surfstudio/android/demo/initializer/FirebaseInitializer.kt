/**
 * Copyright © 2022 Surf. All rights reserved.
 */
@file:Suppress("unused")

package ru.surfstudio.android.demo.initializer

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import ru.surfstudio.android.demo.BuildConfig

/**
 * Initialization Firebase components
 */
class FirebaseInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        FirebaseAnalytics.getInstance(context).setAnalyticsCollectionEnabled(isNotDebug())
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(isNotDebug())
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()

    private fun isNotDebug(): Boolean = !BuildConfig.BUILD_TYPE.contains("debug", ignoreCase = true)
}
