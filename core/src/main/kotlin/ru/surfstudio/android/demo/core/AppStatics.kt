/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.core

import android.annotation.SuppressLint
import ru.surfstudio.android.activity.holder.ActiveActivityHolder

object AppStatics {
    /** clearActivity is called in app lifecycle callback, so there is no memory leak */
    @SuppressLint("StaticFieldLeak")
    val activityHolder = ActiveActivityHolder()
}