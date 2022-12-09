/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.feed.feed

import androidx.compose.runtime.Stable
import dagger.hilt.android.scopes.ViewModelScoped
import ru.surfstudio.mvi.mappers.handler.ErrorHandler
import ru.surfstudio.mvi.mappers.handler.ErrorHandlerReducer
import javax.inject.Inject

@Stable
data class FeedState(
    val sharedResultScreenParentId: String = "sharedResultScreenParentId",
    val bottomSheetParentId: String = "bottomSheetParentId",
    val showDialog: Boolean = false
)

@ViewModelScoped
class FeedReducer @Inject constructor(
    override val errorHandler: ErrorHandler
) : ErrorHandlerReducer<FeedEvent, FeedState> {

    override fun reduce(
        state: FeedState,
        event: FeedEvent
    ): FeedState {
        return when (event) {
            is FeedEvent.OnDialogStateChanged -> state.copy(showDialog = event.show)
            else -> state
        }
    }
}