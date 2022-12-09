/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.components.compose.placeholder.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import ru.surfstudio.android.demo.components.compose.overload.texts.Text_Title_1_Bold_32
import ru.surfstudio.android.demo.components.compose.theme.SurfMviDemoTheme
import ru.surfstudio.compose.modifier.noRippleClickable

@Composable
internal fun TransparentLoadStatePresentation() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.5f)
            .noRippleClickable { /* forbid clicks behind presentation */ },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text_Title_1_Bold_32(text = "TransparentLoadStatePresentation")
    }
}

@Preview(showBackground = true, device = Devices.PIXEL, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun Preview() {
    SurfMviDemoTheme {
        Surface {
            TransparentLoadStatePresentation()
        }
    }
}