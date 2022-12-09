/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog.result

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
import ru.surfstudio.android.demo.components.compose.overload.buttons.ButtonMainBig
import ru.surfstudio.android.demo.components.compose.overload.buttons.ButtonSecondBig
import ru.surfstudio.android.demo.components.compose.theme.SurfMviDemoTheme
import ru.surfstudio.mvi.vm.compose.renders

@Destination
@Composable
fun ResultBusScreen(
    // passed here for destination code generation
    @Suppress("UNUSED_PARAMETER") parentId: String,
    onClose: () -> Unit,
    viewModel: ResultBusViewModel = hiltViewModel()
) {
    viewModel renders { state ->
        ResultBusBody(
            sendResult = {
                emit(ResultBusEvent.SendResult)
            },
            sendResultAndClose = {
                emit(ResultBusEvent.SendResult)
                onClose()
            }
        )
    }
}

@Composable
private fun ResultBusBody(
    sendResult: () -> Unit = {},
    sendResultAndClose: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        val modifier = Modifier.padding(start = 16.dp, top = 40.dp)
        ButtonMainBig(
            text = "Send result",
            onClick = { sendResult() },
            modifier = modifier
        )
        ButtonSecondBig(
            text = "Send result and close",
            onClick = { sendResultAndClose() },
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
            ResultBusBody()
        }
    }
}