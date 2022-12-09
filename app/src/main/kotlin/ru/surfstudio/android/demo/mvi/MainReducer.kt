/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.mvi

import androidx.compose.runtime.Stable
import dagger.hilt.android.scopes.ViewModelScoped
import ru.surfstudio.android.demo.core.mvi.mapper.RequestMappers
import ru.surfstudio.mvi.mappers.RequestEvent
import ru.surfstudio.mvi.mappers.RequestMapper
import ru.surfstudio.mvi.mappers.RequestUi
import ru.surfstudio.mvi.mappers.handler.ErrorHandler
import ru.surfstudio.mvi.mappers.handler.ErrorHandlerReducer
import javax.inject.Inject

@Stable
data class MainState(
    val productsCount: Int = 0
)

@ViewModelScoped
class MainReducer @Inject constructor(
    override val errorHandler: ErrorHandler
) : ErrorHandlerReducer<MainEvent, MainState> {

    override fun reduce(state: MainState, event: MainEvent): MainState {
        return when (event) {
            is MainEvent.GetProductsCountRequest -> state.copy(
                productsCount = updateRequestUi(event).data ?: state.productsCount
            )
            else -> state
        }
    }

    private fun <T : Any> updateRequestUi(event: RequestEvent<T>): RequestUi<T> {
        return RequestMapper
            .builder(event.request)
            .mapData(RequestMappers.data.default())
            .handleError(RequestMappers.error.forced(errorHandler))
            .build()
    }
}