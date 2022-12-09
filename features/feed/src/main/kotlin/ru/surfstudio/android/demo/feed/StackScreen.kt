/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.feed

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
import com.ramcosta.composedestinations.annotation.Destination
import ru.surfstudio.android.demo.components.compose.base.texts.AppText
import ru.surfstudio.android.demo.components.compose.overload.buttons.ButtonMainMedium
import ru.surfstudio.android.demo.components.compose.theme.SurfMviDemoTheme
import ru.surfstudio.android.demo.feed.navigation.StackScreenNavigation

sealed class StackNavigationType {
    object OpenNext : StackNavigationType()
    object ClearStack : StackNavigationType()
}

@Destination
@Composable
fun StackScreen(title: String, navigation: StackScreenNavigation) {
    StackBody(
        title = title,
        openNextScreen = { navigation.navigate(StackNavigationType.OpenNext) },
        clearStack = { navigation.navigate(StackNavigationType.ClearStack) }
    )
}

@Composable
private fun StackBody(
    title: String,
    openNextScreen: () -> Unit = {},
    clearStack: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        val modifier = Modifier.padding(top = 30.dp, start = 16.dp)
        AppText(text = title, modifier = modifier)
        ButtonMainMedium(
            modifier = modifier,
            onClick = { openNextScreen() },
            text = "Open next screen"
        )
        ButtonMainMedium(
            modifier = modifier,
            onClick = { clearStack() },
            text = "Clear stack"
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun Preview() {
    SurfMviDemoTheme {
        Surface {
            StackBody(title = "Test")
        }
    }
}