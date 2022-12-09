/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.debug.ui.drawer

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alorma.drawer_base.DebugDrawerLayout
import com.alorma.drawer_modules.BuildModule
import com.alorma.drawer_modules.DeviceModule
import ru.surfstudio.android.demo.domain.ServerType

@Composable
internal fun DebugDrawerWrapper(
    serverType: ServerType,
    onServerChange: (ServerType, Context) -> Unit = { _, _ -> },
    bodyContent: @Composable (isDrawerOpen: Boolean) -> Unit
) {
    DebugDrawerLayout(
        drawerModules = {
            val modulesModifier = Modifier.padding(4.dp)
            DebugDrawerModules(modulesModifier, serverType, onServerChange)
            BuildModule(modulesModifier)
            DeviceModule(modulesModifier)
        },
        bodyContent = { drawerState ->
            Box {
                bodyContent(drawerState.isOpen)
            }
        }
    )
}