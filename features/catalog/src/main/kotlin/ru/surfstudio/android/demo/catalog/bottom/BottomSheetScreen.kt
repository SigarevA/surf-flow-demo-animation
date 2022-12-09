/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog.bottom

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyle
import ru.surfstudio.android.demo.components.compose.overload.buttons.ButtonMainBig
import ru.surfstudio.android.demo.components.compose.overload.texts.Text_Headline_Regular_16
import ru.surfstudio.android.demo.components.compose.theme.SurfMviDemoTheme
import ru.surfstudio.android.demo.core.Constants.bottomSheetNavigationKey
import ru.surfstudio.mvi.vm.compose.renders

@Destination(
    style = DestinationStyle.BottomSheet::class,
    route = "${bottomSheetNavigationKey}Screen"
)
@Composable
fun BottomSheetScreen(
    // passed here for destination code generation
    @Suppress("UNUSED_PARAMETER") parentId: String,
    onClose: () -> Unit,
    viewModel: BottomSheetViewModel = hiltViewModel()
) {
    viewModel renders { state ->
        BottomSheetBody(
            onAction = {
                emit(BottomSheetEvent.Action)
                onClose()
            }
        )
    }
}

@Composable
private fun BottomSheetBody(onAction: () -> Unit = {}) {
    Column(modifier = Modifier.fillMaxSize()) {
        val modifier = Modifier.padding(start = 16.dp, top = 40.dp)
        Text_Headline_Regular_16(text = "BottomSheet destination sample")
        ButtonMainBig(
            text = "Perform action",
            onClick = { onAction() },
            modifier = modifier
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun Preview() {
    SurfMviDemoTheme {
        Surface {
            BottomSheetBody()
        }
    }
}