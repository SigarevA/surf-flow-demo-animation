/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo

import android.app.Activity
import android.app.Application
import android.os.Bundle
import dagger.hilt.android.HiltAndroidApp
import ru.surfstudio.android.demo.core.AppStatics
import ru.surfstudio.android.demo.debug.DebugSettings

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        registerActiveActivityListener()
        DebugSettings.showNotification(this)
    }

    private fun registerActiveActivityListener() {
        registerActivityLifecycleCallbacks(
            object : ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

                override fun onActivityStarted(activity: Activity) {}

                override fun onActivityResumed(activity: Activity) {
                    AppStatics.activityHolder.activity = activity
                }

                override fun onActivityPaused(activity: Activity) {
                    AppStatics.activityHolder.clearActivity()
                }

                override fun onActivityStopped(activity: Activity) {}

                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

                override fun onActivityDestroyed(activity: Activity) {}
            }
        )
    }
}