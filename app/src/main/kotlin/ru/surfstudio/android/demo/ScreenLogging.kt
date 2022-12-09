/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import ru.surfstudio.android.demo.core.Constants.loggingTag
import ru.surfstudio.android.logger.Logger

/** Log current visible screen & lifecycle */
@Composable
internal fun ScreenLogging(navController: NavController) {
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { value ->
            Logger.d(
                "$loggingTag Screen ${value.destination.route} ${value.arguments}"
            )
        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            Logger.d("$loggingTag Lifecycle ${event.name}")
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}