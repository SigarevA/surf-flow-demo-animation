/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.ramcosta.composedestinations.spec.NavGraphSpec
import ru.surfstudio.android.demo.components.compose.overload.texts.Text_Caption_Regular_12
import ru.surfstudio.android.demo.components.compose.theme.AppTheme
import ru.surfstudio.android.demo.core.Constants.loggingTag
import ru.surfstudio.android.logger.Logger

@Composable
internal fun BottomNavigationBar(navController: NavController, count: Int = 0) {
    val items = MainTab.values()
    BottomNavigation {
        val currentSelectedItem by navController.currentScreenAsState()
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    if (item != MainTab.Basket) {
                        Icon(item)
                    } else {
                        BadgedBox(
                            badge = {
                                if (count > 0) {
                                    Badge {
                                        Text_Caption_Regular_12(
                                            text = count.toString(),
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        ) {
                            Icon(item)
                        }
                    }
                },
                label = {
                    // use Text so selectedContentColor and unselectedContentColor work
                    // otherwise change text color manually
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = item.titleResId),
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        style = AppTheme.typography.Caption_Regular_12
                    )
                },
                selectedContentColor = AppTheme.colors.Red,
                unselectedContentColor = AppTheme.colors.Steel_Gray_300,
                alwaysShowLabel = true,
                selected = currentSelectedItem == item.screen,
                onClick = {
                    navController.navigate(item.screen.route) {
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true

                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun Icon(item: MainTab) {
    Icon(
        modifier = Modifier
            .size(24.dp)
            .padding(2.dp),
        painter = painterResource(id = item.iconResId),
        contentDescription = stringResource(id = item.titleResId)
    )
}

/**
 * Adds an [NavController.OnDestinationChangedListener] to this [NavController] and updates the
 * returned [State] which is updated as the destination changes.
 */
@Stable
@Composable
private fun NavController.currentScreenAsState(): State<NavGraphSpec> {
    val selectedItem = remember { mutableStateOf(NavGraphs.feed) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            backQueue.print()
            selectedItem.value = destination.navGraph()
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}

private fun ArrayDeque<NavBackStackEntry>.print(prefix: String = "stack") {
    val stack = toMutableList()
        .map { it.destination.route }
        .toTypedArray()
        .contentToString()
    Logger.d("$loggingTag $prefix = $stack")
}

internal fun NavDestination.navGraph(): NavGraphSpec {
    hierarchy.forEach { destination ->
        NavGraphs.root.nestedNavGraphs.forEach { navGraph ->
            if (destination.route == navGraph.route) {
                return navGraph
            }
        }
    }
    throw RuntimeException("Unknown nav graph for destination $route")
}