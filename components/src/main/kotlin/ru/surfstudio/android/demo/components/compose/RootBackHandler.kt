/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.components.compose

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import ru.surfstudio.android.demo.core.Constants.bottomSheetNavigationKey

/** Required for root of each nested nav graph */
@Composable
fun RootBackHandler() {
    val activity = (LocalContext.current as? Activity)
    BackHandler {
        activity?.finish()
    }
}

@Composable
fun RootBackHandler(navController: NavController) {
    val activity = (LocalContext.current as? Activity)
    var currentRoute by remember { mutableStateOf(navController.currentDestination?.route) }

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { value ->
            currentRoute = value.destination.route
        }
    }
    BackHandler {
        if (currentRoute?.contains(bottomSheetNavigationKey) == true) {
            navController.navigateUp()
        } else {
            activity?.finish()
        }
    }
}