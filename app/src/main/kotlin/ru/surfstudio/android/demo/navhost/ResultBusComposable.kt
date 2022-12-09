/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.navhost

import com.ramcosta.composedestinations.manualcomposablecalls.ManualComposableCallsBuilder
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import ru.surfstudio.android.demo.catalog.destinations.ResultBusScreenDestination
import ru.surfstudio.android.demo.catalog.result.ResultBusScreen

internal fun ManualComposableCallsBuilder.resultBusComposable(
    onClose: () -> Unit
) {
    composable(ResultBusScreenDestination) {
        ResultBusScreen(
            parentId = navArgs.parentId,
            onClose = onClose
        )
    }
}