/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.surfstudio.android.demo.core.Constants.fullScreenNavigationKey

/**
 * Responsible for holding state related to [MainScreen] and containing UI-related logic.
 */
@Stable
class MainComposableState(private val navController: NavHostController) {

    // Reading this attribute will cause recompositions when the bottom bar needs shown, or not.
    // Not all routes need to show the bottom bar.
    val shouldShowBottomBar: Boolean
        @Composable get() {
            val currentRoute = navController
                .currentBackStackEntryAsState().value?.destination?.route.orEmpty()
            return !currentRoute.contains(fullScreenNavigationKey)
        }
}

@Composable
fun rememberMainComposableState(
    navController: NavHostController
) = remember(navController) { MainComposableState(navController) }