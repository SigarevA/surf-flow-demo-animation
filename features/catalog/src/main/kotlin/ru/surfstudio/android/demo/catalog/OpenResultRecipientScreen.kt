/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import ru.surfstudio.android.demo.components.compose.overload.buttons.ButtonMainBig

@Destination
@Composable
fun OpenResultRecipientScreen(
    resultNavigator: ResultBackNavigator<Boolean>
) {
    Box(modifier = Modifier.fillMaxSize()) {
        ButtonMainBig(
            onClick = {
                resultNavigator.navigateBack(true)
            },
            modifier = Modifier.align(Alignment.Center),
            text = "Success result recipient!"
        )
    }
}