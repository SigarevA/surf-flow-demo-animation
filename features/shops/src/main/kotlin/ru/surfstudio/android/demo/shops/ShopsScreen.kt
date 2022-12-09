/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.shops

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import ru.surfstudio.android.demo.components.compose.RootBackHandler
import ru.surfstudio.android.demo.components.compose.base.texts.AppText
import ru.surfstudio.android.demo.components.compose.theme.SurfMviDemoTheme

@Destination
@Composable
fun ShopsScreen() {
    RootBackHandler()
    Box(modifier = Modifier.fillMaxSize()) {
        AppText(text = "Shops", modifier = Modifier.align(Alignment.Center))
    }
}

@Preview(showBackground = true, device = Devices.PIXEL, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun Preview() {
    SurfMviDemoTheme {
        Surface {
            ShopsScreen()
        }
    }
}