/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.debug.ui.drawer

import android.content.Context
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.alorma.drawer_modules.ActionsModule
import com.alorma.drawer_modules.actions.DropdownSelectorAction
import com.alorma.drawer_modules.actions.TextAction
import ru.surfstudio.android.demo.domain.ServerType

/**
 * Debug drawer custom options
 */
@Composable
internal fun DebugDrawerModules(
    modifier: Modifier = Modifier,
    serverType: ServerType,
    onServerChange: (ServerType, Context) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    val dropdownItems = ServerType.values().map { it.name }

    ActionsModule(
        modifier = modifier,
        icon = { Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings") },
        title = "Custom settings"
    ) {
        TextAction(text = "Server Type")
        DropdownSelectorAction(
            label = "Server types",
            items = dropdownItems,
            defaultValue = serverType.name,
            onItemSelected = { item -> onServerChange(ServerType.valueOf(item), context) }
        )
    }
}