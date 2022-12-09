/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.basket

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import ru.surfstudio.android.demo.components.compose.RootBackHandler
import ru.surfstudio.android.demo.components.compose.base.texts.AppText
import ru.surfstudio.android.demo.components.compose.overload.buttons.ButtonMainBig
import ru.surfstudio.android.demo.components.compose.theme.SurfMviDemoTheme
import ru.surfstudio.mvi.vm.compose.renders

@Destination
@Composable
fun BasketScreen(
    viewModel: BasketViewModel = hiltViewModel()
) {
    RootBackHandler()
    viewModel renders { state ->
        BasketBody(
            state = state,
            onReloadClick = { emit(BasketEvent.ReloadClicked) }
        )
    }
}

@Composable
fun BasketBody(state: BasketState, onReloadClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppText(text = state.uiState)
        ButtonMainBig(onClick = onReloadClick, text = "Reload")
    }
}

@Preview(showBackground = true, device = Devices.PIXEL, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun Preview() {
    SurfMviDemoTheme {
        Surface {
            BasketBody(state = BasketState(), onReloadClick = {})
        }
    }
}