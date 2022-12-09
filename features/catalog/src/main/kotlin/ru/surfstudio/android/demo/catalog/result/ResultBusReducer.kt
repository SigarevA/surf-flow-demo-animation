/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog.result

import androidx.compose.runtime.Stable
import dagger.hilt.android.scopes.ViewModelScoped
import ru.surfstudio.mvi.mappers.handler.ErrorHandler
import ru.surfstudio.mvi.mappers.handler.ErrorHandlerReducer
import javax.inject.Inject

@Stable
data class ResultBusState(
    val parentId: String
)

@ViewModelScoped
class ResultBusReducer @Inject constructor(
    override val errorHandler: ErrorHandler
) : ErrorHandlerReducer<ResultBusEvent, ResultBusState> {

    override fun reduce(
        state: ResultBusState,
        event: ResultBusEvent
    ): ResultBusState {
        return when (event) {
            else -> state
        }
    }
}