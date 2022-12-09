/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog

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
import ru.surfstudio.android.demo.catalog.navigation.CatalogScreenNavigation
import ru.surfstudio.android.demo.components.compose.RootBackHandler
import ru.surfstudio.android.demo.components.compose.overload.buttons.ButtonMainBig
import ru.surfstudio.android.demo.components.compose.overload.buttons.ButtonSecondBig
import ru.surfstudio.android.demo.components.compose.theme.SurfMviDemoTheme

sealed class CatalogNavigationType {
    object OpenCategory : CatalogNavigationType()
    object OpenDogs : CatalogNavigationType()
    object OpenCats : CatalogNavigationType()
}

@Destination
@Composable
fun CatalogScreen(navigation: CatalogScreenNavigation) {
    RootBackHandler()
    CatalogBody(
        openDogs = { navigation.navigate(CatalogNavigationType.OpenDogs) },
        openCats = { navigation.navigate(CatalogNavigationType.OpenCats) },
        openCategory = { navigation.navigate(CatalogNavigationType.OpenCategory) },
    )
}

@Composable
private fun CatalogBody(
    openDogs: () -> Unit = {},
    openCats: () -> Unit = {},
    openCategory: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        val modifier = Modifier.padding(start = 16.dp, top = 40.dp)
        ButtonMainBig(
            text = "Open dogs catalog",
            onClick = { openDogs() },
            modifier = modifier
        )
        ButtonMainBig(
            text = "Open cats catalog",
            onClick = { openCats() },
            modifier = modifier
        )
        ButtonSecondBig(
            text = "Open full screen  category",
            onClick = { openCategory() },
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
            CatalogBody()
        }
    }
}