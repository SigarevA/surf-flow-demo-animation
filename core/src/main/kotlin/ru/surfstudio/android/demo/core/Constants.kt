/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.core

object Constants {
    /**
     * Part of route for screens which don't need to keep bottom navigation.
     * Used for filter of routes when the bottomBar should be hidden
     */
    const val fullScreenNavigationKey = "FullScreen"

    /**
     * Part of route for bottom sheet screens.
     * Used to implement bottom sheet close action on back pressed
     */
    const val bottomSheetNavigationKey = "BottomSheet"

    const val loggingTag = "Logging"

    const val okhttpTag = "OkHttp"

    const val pageSize = 20
}