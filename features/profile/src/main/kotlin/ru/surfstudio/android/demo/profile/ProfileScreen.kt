/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.profile

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
import ru.surfstudio.android.demo.components.compose.overload.buttons.ButtonBorderlessBig
import ru.surfstudio.android.demo.components.compose.placeholder.PlaceholderContainer
import ru.surfstudio.android.demo.components.compose.theme.SurfMviDemoTheme
import ru.surfstudio.mvi.vm.compose.renders

@Destination
@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    RootBackHandler()
    viewModel renders { state ->
        ProfileBody(
            state = state,
            action = {
                emit(
                    ProfileEvent.LogoutClicked.takeIf { state.isAuthorized }
                        ?: ProfileEvent.LoginClicked
                )
            }
        )
    }
}

@Composable
private fun ProfileBody(state: ProfileState, action: () -> Unit) {
    PlaceholderContainer(loadStateType = state.loadState, retry = action) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AppText(text = state.uiState)
            ButtonBorderlessBig(
                onClick = action,
                text = "Logout".takeIf { state.isAuthorized } ?: "Login"
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun Preview() {
    SurfMviDemoTheme {
        Surface {
            ProfileBody(state = ProfileState(), action = {})
        }
    }
}