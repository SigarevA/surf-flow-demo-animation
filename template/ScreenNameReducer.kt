/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.template

import androidx.compose.runtime.Stable
import dagger.hilt.android.scopes.ViewModelScoped
import ru.surfstudio.mvi.mappers.handler.ErrorHandler
import ru.surfstudio.mvi.mappers.handler.ErrorHandlerReducer
import javax.inject.Inject

@Stable
class ScreenNameState

@ViewModelScoped
class ScreenNameReducer @Inject constructor(
    override val errorHandler: ErrorHandler
) : ErrorHandlerReducer<ScreenNameEvent, ScreenNameState> {

    override fun reduce(
        state: ScreenNameState,
        event: ScreenNameEvent
    ): ScreenNameState {
        return when (event) {
            else -> state
        }
    }
}