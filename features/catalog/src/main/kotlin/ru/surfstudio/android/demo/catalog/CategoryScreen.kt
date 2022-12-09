/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog

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
import ru.surfstudio.android.demo.components.compose.overload.texts.Text_Title_1_Bold_32
import ru.surfstudio.android.demo.components.compose.theme.SurfMviDemoTheme
import ru.surfstudio.android.demo.core.Constants.fullScreenNavigationKey

@Destination(
    route = "${fullScreenNavigationKey}Category"
)
@Composable
fun FullCategoryScreen(name: String) {
    CategoryBody(name = name)
}

@Destination
@Composable
fun CategoryScreen(name: String) {
    CategoryBody(name = name)
}

@Composable
private fun CategoryBody(name: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text_Title_1_Bold_32(
            text = name,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun Preview() {
    SurfMviDemoTheme {
        Surface {
            CategoryBody(name = "32 Title 1 Bold")
        }
    }
}