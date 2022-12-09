/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog.bottom

import androidx.compose.runtime.Stable
import dagger.hilt.android.scopes.ViewModelScoped
import ru.surfstudio.mvi.mappers.handler.ErrorHandler
import ru.surfstudio.mvi.mappers.handler.ErrorHandlerReducer
import javax.inject.Inject

@Stable
data class BottomSheetState(
    val parentId: String
)

@ViewModelScoped
class BottomSheetReducer @Inject constructor(
    override val errorHandler: ErrorHandler
) : ErrorHandlerReducer<BottomSheetEvent, BottomSheetState> {

    override fun reduce(
        state: BottomSheetState,
        event: BottomSheetEvent
    ): BottomSheetState {
        return when (event) {
            else -> state
        }
    }
}