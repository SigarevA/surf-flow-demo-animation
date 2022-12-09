/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.components.compose.base.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.surfstudio.android.demo.components.compose.theme.AppTheme

/**
 * Base container for loading button content,
 * can be used inside of `Button` on `TextButton`
 */
@Composable
private fun LoadingContainer(
    isLoading: Boolean = false,
    loadingContent: @Composable () -> Unit,
    textContent: @Composable () -> Unit
) {
    Box {
        // show invisible content for loading in order to set up the same width
        Box(modifier = Modifier.alpha(0f.takeIf { isLoading } ?: 1f)) {
            textContent()
        }
        if (isLoading) {
            Box(modifier = Modifier.align(Alignment.Center)) {
                loadingContent()
            }
        }
    }
}

@Composable
internal fun ButtonLoadingIndicator(
    isLoading: Boolean = false,
    loadingColor: Color = AppTheme.colors.Turquoise,
    textContent: @Composable () -> Unit
) {
    LoadingContainer(
        isLoading = isLoading,
        loadingContent = {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = loadingColor,
                strokeWidth = 2.dp
            )
        },
        textContent = textContent
    )
}