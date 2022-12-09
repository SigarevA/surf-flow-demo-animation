/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.basket.sample

import androidx.compose.runtime.Stable
import dagger.hilt.android.scopes.ViewModelScoped
import ru.surfstudio.android.demo.core.mvi.mapper.LoadStateType
import ru.surfstudio.android.demo.core.mvi.mapper.RequestMappers
import ru.surfstudio.mvi.mappers.RequestEvent
import ru.surfstudio.mvi.mappers.RequestMapper
import ru.surfstudio.mvi.mappers.handler.ErrorHandler
import ru.surfstudio.mvi.mappers.handler.ErrorHandlerReducer
import javax.inject.Inject

/** Sample screen for unit tests */
@Stable
data class SampleState(
    val loadState: LoadStateType = LoadStateType.None
)

/** Sample screen for unit tests */
@ViewModelScoped
class SampleReducer @Inject constructor(
    override val errorHandler: ErrorHandler
) : ErrorHandlerReducer<SampleEvent, SampleState> {

    override fun reduce(state: SampleState, event: SampleEvent): SampleState {
        return when (event) {
            is SampleEvent.DataLoad.GetProductsCountRequest -> state.updateRequestUi(event)
            else -> state
        }
    }

    private fun <T : Any> SampleState.updateRequestUi(event: RequestEvent<T>): SampleState {
        return copy(
            loadState = RequestMapper
                .builder(event.request)
                .mapData(RequestMappers.data.default())
                .mapLoading(RequestMappers.loading.default())
                .handleError(RequestMappers.error.forced(errorHandler))
                .build()
                .load as? LoadStateType ?: loadState
        )
    }
}