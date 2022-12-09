/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.components.compose.placeholder

import androidx.compose.runtime.Composable
import ru.surfstudio.android.demo.components.compose.placeholder.presentation.*
import ru.surfstudio.android.demo.core.mvi.mapper.LoadStateType

/**
 * Base composable for rendering load states.
 * Each visible load state presentation can be customized.
 *
 * @param retry action callback for using in presentations which require such callback.
 * Used for default presentations only,
 * each custom action presentation must be initialized with its own callback
 */
@Composable
fun PlaceholderView(
    loadState: LoadStateType,
    customErrorPresentation: @Composable (() -> Unit)? = null,
    customNoInternetPresentation: @Composable (() -> Unit)? = null,
    customMainLoadPresentation: @Composable (() -> Unit)? = null,
    customEmptyPresentation: @Composable (() -> Unit)? = null,
    customTransparentLoadPresentation: @Composable (() -> Unit)? = null,
    retry: () -> Unit
) {
    when (loadState) {
        LoadStateType.Error -> if (customErrorPresentation != null) {
            customErrorPresentation()
        } else {
            ErrorLoadStatePresentation(retry = retry)
        }
        LoadStateType.NoInternet -> if (customNoInternetPresentation != null) {
            customNoInternetPresentation()
        } else {
            NoInternetLoadStatePresentation(retry = retry)
        }
        LoadStateType.Main -> if (customMainLoadPresentation != null) {
            customMainLoadPresentation()
        } else {
            MainLoadStatePresentation()
        }
        LoadStateType.Empty -> if (customEmptyPresentation != null) {
            customEmptyPresentation()
        } else {
            EmptyLoadStatePresentation()
        }
        LoadStateType.TransparentLoading -> if (customTransparentLoadPresentation != null) {
            customTransparentLoadPresentation()
        } else {
            TransparentLoadStatePresentation()
        }
        LoadStateType.SwipeRefreshLoading, LoadStateType.None -> {
            /* render nothing; the usual screen content is shown */
        }
    }
}