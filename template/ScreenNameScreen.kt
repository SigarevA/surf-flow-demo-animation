/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.template

import android.content.res.Configuration
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import ru.surfstudio.android.demo.components.compose.theme.SurfMviDemoTheme
import ru.surfstudio.mvi.vm.compose.renders

@Destination
@Composable
fun ScreenNameScreen(
    viewModel: ScreenNameViewModel = hiltViewModel()
) {
    viewModel renders { state ->
        ScreenNameBody()
    }
}

@Composable
private fun ScreenNameBody(onAction: () -> Unit = {}) {
    Text("Template")
}

@Preview(showBackground = true, device = Devices.PIXEL, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun Preview() {
    SurfMviDemoTheme {
        Surface {
            ScreenNameBody()
        }
    }
}