/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.feed.feed

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.OpenResultRecipient
import com.ramcosta.composedestinations.result.ResultRecipient
import ru.surfstudio.android.demo.components.compose.RootBackHandler
import ru.surfstudio.android.demo.components.compose.base.dialogs.BaseAppDialog
import ru.surfstudio.android.demo.components.compose.overload.buttons.ButtonCancelBig
import ru.surfstudio.android.demo.components.compose.overload.buttons.ButtonMainBig
import ru.surfstudio.android.demo.components.compose.overload.buttons.ButtonSecondBig
import ru.surfstudio.android.demo.components.compose.theme.SurfMviDemoTheme
import ru.surfstudio.android.demo.feed.destinations.ResultScreenDestination
import ru.surfstudio.android.demo.feed.navigation.FeedScreenNavigation
import ru.surfstudio.mvi.vm.compose.renders

sealed class FeedNavigationType {
    object OpenCategory : FeedNavigationType()
    object OpenNext : FeedNavigationType()
    object OpenForResultSimpleRecipient : FeedNavigationType()
    object OpenForResultOpenRecipient : FeedNavigationType()
    data class OpenBottomSheet(val parentId: String) : FeedNavigationType()
    data class OpenForResultBus(val parentId: String) : FeedNavigationType()
}

@Destination
@Composable
fun FeedScreen(
    navController: NavController,
    navigation: FeedScreenNavigation,
    onResult: (Boolean) -> Unit,
    viewModel: FeedViewModel = hiltViewModel(),
    resultRecipient: ResultRecipient<ResultScreenDestination, Boolean>,
    openResultRecipient: OpenResultRecipient<Boolean>
) {
    RootBackHandler(navController)
    resultRecipient.onNavResult { result ->
        when (result) {
            NavResult.Canceled -> onResult(false)
            is NavResult.Value -> onResult(result.value)
        }
    }
    openResultRecipient.onNavResult { result ->
        when (result) {
            NavResult.Canceled -> onResult(false)
            is NavResult.Value -> onResult(result.value)
        }
    }
    viewModel renders { state ->
        FeedBody(
            state = state,
            openCategory = { navigation.navigate(FeedNavigationType.OpenCategory) },
            openStack = { navigation.navigate(FeedNavigationType.OpenNext) },
            openForResultSimpleRecipient = { navigation.navigate(FeedNavigationType.OpenForResultSimpleRecipient) },
            openForResultOpenRecipient = { navigation.navigate(FeedNavigationType.OpenForResultOpenRecipient) },
            openForResultBus = { navigation.navigate(FeedNavigationType.OpenForResultBus(state.sharedResultScreenParentId)) },
            openBottomSheet = { navigation.navigate(FeedNavigationType.OpenBottomSheet(state.bottomSheetParentId)) },
            dialogStateChanged = { emit(FeedEvent.OnDialogStateChanged(show = it)) }
        )
    }
}

@Composable
private fun FeedBody(
    state: FeedState,
    openCategory: () -> Unit = {},
    openStack: () -> Unit = {},
    openForResultSimpleRecipient: () -> Unit = {},
    openForResultOpenRecipient: () -> Unit = {},
    openForResultBus: () -> Unit = {},
    openBottomSheet: () -> Unit = {},
    dialogStateChanged: (Boolean) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val modifier = Modifier.padding(start = 16.dp, top = 40.dp)
        ButtonMainBig(
            text = "Open simple category",
            onClick = { openCategory() },
            modifier = modifier
        )
        ButtonSecondBig(
            text = "Open stack",
            onClick = { openStack() },
            modifier = modifier
        )
        ButtonCancelBig(
            text = "Open for result simple recipient",
            onClick = { openForResultSimpleRecipient() },
            modifier = modifier
        )
        ButtonCancelBig(
            text = "Open for result open recipient",
            onClick = { openForResultOpenRecipient() },
            modifier = modifier
        )
        ButtonMainBig(
            text = "Open for result bus",
            onClick = { openForResultBus() },
            modifier = modifier
        )
        ButtonSecondBig(
            text = "Open simple dialog",
            onClick = { dialogStateChanged(true) },
            modifier = modifier
        )
        ButtonSecondBig(
            text = "Open BottomSheet",
            onClick = { openBottomSheet() },
            modifier = modifier
        )
    }

    if (state.showDialog) {
        BaseAppDialog(
            title = "Title",
            positiveText = "OK",
            positiveClicked = { dialogStateChanged(false) },
            onDismissRequest = { dialogStateChanged(false) }
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun Preview() {
    SurfMviDemoTheme {
        Surface {
            FeedBody(FeedState())
        }
    }
}