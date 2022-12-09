/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.navhost

import com.ramcosta.composedestinations.manualcomposablecalls.ManualComposableCallsBuilder
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import ru.surfstudio.android.demo.catalog.bottom.BottomSheetScreen
import ru.surfstudio.android.demo.catalog.destinations.BottomSheetScreenDestination

internal fun ManualComposableCallsBuilder.bottomSheetComposable(
    onClose: () -> Unit
) {
    composable(BottomSheetScreenDestination) {
        BottomSheetScreen(
            parentId = navArgs.parentId,
            onClose = onClose
        )
    }
}