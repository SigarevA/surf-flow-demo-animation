/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.debug

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dagger.hilt.android.AndroidEntryPoint
import ru.surfstudio.android.demo.components.compose.base.texts.AppText
import ru.surfstudio.android.demo.components.compose.theme.SurfMviDemoTheme
import ru.surfstudio.android.demo.debug.ui.drawer.DebugDrawerWrapper

@AndroidEntryPoint
internal class DebugActivity : ComponentActivity() {

    private val viewModel: DebugViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SurfMviDemoTheme {
                Surface {
                    val currentServerType by viewModel.serverType.collectAsState()
                    DebugDrawerWrapper(
                        serverType = currentServerType,
                        onServerChange = { serverType, context ->
                            viewModel.setServerType(serverType, context)
                        }
                    ) {
                        //todo add item to app settings
                        AppText(currentServerType.name)
                    }
                }
            }
        }
    }
}