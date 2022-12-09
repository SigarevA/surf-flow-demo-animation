/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.plusAssign
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.scope.DestinationScope
import com.ramcosta.composedestinations.spec.NavGraphSpec
import ru.surfstudio.android.demo.mvi.MainEvent
import ru.surfstudio.android.demo.mvi.MainViewModel
import ru.surfstudio.android.demo.navhost.app.AppNavigation
import ru.surfstudio.android.demo.navhost.bottomSheetComposable
import ru.surfstudio.android.demo.navhost.feedComposable
import ru.surfstudio.android.demo.navhost.resultBusComposable
import ru.surfstudio.mvi.vm.compose.renders

@ExperimentalMaterialNavigationApi
@ExperimentalAnimationApi
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    showMessage: (String, Boolean) -> Unit
) {
    val navController = rememberAnimatedNavController()
    val mainComposableState = rememberMainComposableState(navController = navController)

    val bottomSheetNavigator = rememberBottomSheetNavigator()
    navController.navigatorProvider += bottomSheetNavigator

    viewModel renders { state ->
        ScreenLogging(navController = navController)
        ModalBottomSheetLayout(bottomSheetNavigator = bottomSheetNavigator) {
            Scaffold(
                bottomBar = {
                    if (mainComposableState.shouldShowBottomBar) {
                        BottomNavigationBar(
                            navController = navController,
                            count = state.productsCount
                        )
                    } else {
                        // close previous snack for a full screen view
                        emit(MainEvent.CloseSnack)
                    }
                }
            ) { paddingValues ->
                AppNavHost(
                    navController = navController,
                    showMessage = showMessage,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
@Composable
private fun AppNavHost(
    navController: NavHostController,
    showMessage: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val onClose: () -> Unit = { navController.navigateUp() }
    DestinationsNavHost(
        modifier = modifier,
        engine = rememberAnimatedNavHostEngine(
            rootDefaultAnimations = RootNavGraphDefaultAnimations(
                enterTransition = { fadeIn(animationSpec = tween(400)) },
                exitTransition = { fadeOut(animationSpec = tween(400)) }
            )
        ),
        navController = navController,
        navGraph = NavGraphs.root,
        dependenciesContainerBuilder = {
            dependency(appNavigation())
        }
    ) {
        feedComposable(navController = navController, showMessage = showMessage)
        bottomSheetComposable(onClose = onClose)
        resultBusComposable(onClose = onClose)
    }
}

internal fun DestinationScope<*>.navGraph(): NavGraphSpec {
    return navBackStackEntry.destination.navGraph()
}

internal fun DestinationScope<*>.appNavigation(): AppNavigation {
    val appDestinationsNavigator = destinationsNavigator
    val appNavGraph = navGraph()
    return object : AppNavigation {
        override val navGraph: NavGraphSpec = appNavGraph
        override val destinationsNavigator: DestinationsNavigator =
            appDestinationsNavigator
    }
}