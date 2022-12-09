/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.components.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed
import ru.surfstudio.android.demo.components.compose.overload.buttons.ButtonSecondSmall
import ru.surfstudio.android.demo.components.compose.overload.texts.Text_Headline_Regular_16
import ru.surfstudio.android.demo.components.compose.overload.texts.Text_Title_2_Black_20
import ru.surfstudio.android.demo.components.compose.theme.AppTheme
import ru.surfstudio.android.demo.core.mvi.mapper.LoadStateType
import ru.surfstudio.android.demo.core.network.NoInternetException

//region pagination ui items
/** Optional placeholder for null item during loading according to paging3 lib placeholders feature */
@Composable
fun PaginationPlaceholderItem() {
    Text_Title_2_Black_20(text = "PLACEHOLDER")
}

@Composable
fun PaginationLoadingFooter(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun PaginationErrorFooter(
    message: String,
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit
) {
    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text_Headline_Regular_16(
            text = message,
            maxLines = 1,
            modifier = Modifier.weight(1f),
            color = AppTheme.colors.Red
        )
        ButtonSecondSmall(text = "Try again", onClick = onClickRetry)
    }
}
//endregion

//region pagination container
/** Base screen container which handles pagination UI render logic */
@Composable
fun <T : Any> PaginationContainer(
    items: LazyPagingItems<T>,
    showPlaceholders: Boolean = true,
    itemContent: @Composable (T, Int) -> Unit
) {
    LazyColumn {
        itemsIndexed(items) { index, item ->
            if (item != null) {
                itemContent(item, index)
            } else if (showPlaceholders) {
                PaginationPlaceholderItem() // optional
            }
        }
        item {
            PaginationFooter(items = items)
        }
    }
}

/** Base pagination footer logic for app */
@Composable
fun <T : Any> PaginationFooter(items: LazyPagingItems<T>) {
    val isEmpty = items.itemCount == 0
    with(items) {
        when {
            !isEmpty && loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading -> {
                PaginationLoadingFooter()
            }
            !isEmpty && loadState.refresh is LoadState.Error -> {
                val e = loadState.refresh as LoadState.Error
                PaginationErrorFooter(
                    message = e.error.localizedMessage!!,
                    onClickRetry = { retry() }
                )
            }
            loadState.append is LoadState.Error -> {
                val e = items.loadState.append as LoadState.Error
                PaginationErrorFooter(
                    message = e.error.localizedMessage!!,
                    onClickRetry = { retry() }
                )
            }
        }
    }
}
//endregion

/** Get [LoadStateType] for [LazyPagingItems] */
fun <T : Any> LazyPagingItems<T>.mapToLoadStateType(): LoadStateType {
    val isEmpty = itemCount == 0
    return when {
        loadState.refresh is LoadState.Loading -> {
            if (isEmpty) {
                LoadStateType.Main
            } else {
                LoadStateType.SwipeRefreshLoading
            }
        }
        loadState.refresh is LoadState.Error && isEmpty -> {
            val e = loadState.refresh as LoadState.Error
            if (e.error is NoInternetException) {
                LoadStateType.NoInternet
            } else {
                LoadStateType.Error
            }
        }
        loadState.refresh is LoadState.NotLoading && isEmpty -> LoadStateType.Empty
        else -> LoadStateType.None
    }
}