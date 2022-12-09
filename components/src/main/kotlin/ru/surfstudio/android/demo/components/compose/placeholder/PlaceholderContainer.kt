/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.components.compose.placeholder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import ru.surfstudio.android.demo.core.mvi.mapper.LoadStateType
import ru.surfstudio.android.demo.core.util.isNoneOf
import ru.surfstudio.android.demo.core.util.isOneOf

/** Base screen container which has placeholder */
@Composable
fun PlaceholderContainer(
    modifier: Modifier = Modifier,
    loadStateType: LoadStateType,
    retry: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (
            loadStateType.isOneOf(
                LoadStateType.None,
                LoadStateType.SwipeRefreshLoading,
                LoadStateType.TransparentLoading
            )
        ) {
            content()
        }
        if (
            loadStateType.isNoneOf(
                LoadStateType.None,
                LoadStateType.SwipeRefreshLoading
            )
        ) {
            PlaceholderView(loadState = loadStateType, retry = retry)
        }
    }
}

/** Base screen container which has placeholder and swipe refresh */
@Composable
fun PlaceholderSwipeRefreshContainer(
    modifier: Modifier = Modifier,
    loadStateType: LoadStateType,
    refreshState: SwipeRefreshState,
    retry: () -> Unit,
    refresh: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    PlaceholderContainer(
        modifier = modifier,
        loadStateType = loadStateType,
        retry = { retry() }
    ) {
        SwipeRefresh(
            state = refreshState,
            swipeEnabled = loadStateType.isOneOf(
                LoadStateType.None,
                LoadStateType.SwipeRefreshLoading,
                LoadStateType.Empty
            ),
            onRefresh = { refresh() }
        ) {
            content()
        }
    }
}