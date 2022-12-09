/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.navhost

import androidx.navigation.NavController
import com.ramcosta.composedestinations.manualcomposablecalls.ManualComposableCallsBuilder
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.scope.resultRecipient
import ru.surfstudio.android.demo.appNavigation
import ru.surfstudio.android.demo.catalog.destinations.OpenResultRecipientScreenDestination
import ru.surfstudio.android.demo.feed.destinations.FeedScreenDestination
import ru.surfstudio.android.demo.feed.feed.FeedScreen

internal fun ManualComposableCallsBuilder.feedComposable(
    navController: NavController,
    showMessage: (String, Boolean) -> Unit
) {
    composable(FeedScreenDestination) {
        FeedScreen(
            navController = navController,
            navigation = appNavigation(),
            onResult = { result ->
                showMessage("Success!".takeIf { result } ?: "Cancel", result)
            },
            resultRecipient = resultRecipient(),
            openResultRecipient = resultRecipient<OpenResultRecipientScreenDestination, Boolean>()
        )
    }
}