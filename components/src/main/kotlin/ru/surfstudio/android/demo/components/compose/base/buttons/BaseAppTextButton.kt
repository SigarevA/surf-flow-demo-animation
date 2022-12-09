/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.components.compose.base.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import ru.surfstudio.android.demo.components.compose.overload.buttons.bigButtonPaddingValues
import ru.surfstudio.android.demo.components.compose.theme.AppTheme

/** Base app borderless button */
@Composable
internal fun BaseAppTextButton(
    modifier: Modifier = Modifier,
    textContent: @Composable () -> Unit,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    contentPadding: PaddingValues = bigButtonPaddingValues,
    shape: Shape = AppTheme.shapes.button,
    loadingColor: Color = AppTheme.colors.Turquoise,
    onClick: () -> Unit = {}
) {
    TextButton(
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        ),
        enabled = isEnabled && !isLoading,
        onClick = onClick,
        modifier = modifier,
        contentPadding = contentPadding,
        shape = shape,
        colors = colors
    ) {
        ButtonLoadingIndicator(
            isLoading = isLoading,
            loadingColor = loadingColor,
            textContent = textContent
        )
    }
}