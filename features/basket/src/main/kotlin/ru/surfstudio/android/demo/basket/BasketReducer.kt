/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.basket

import androidx.compose.runtime.Stable
import dagger.hilt.android.scopes.ViewModelScoped
import ru.surfstudio.android.demo.core.mvi.mapper.LoadStateType
import ru.surfstudio.android.demo.core.mvi.mapper.RequestMappers
import ru.surfstudio.mvi.mappers.RequestEvent
import ru.surfstudio.mvi.mappers.RequestMapper
import ru.surfstudio.mvi.mappers.handler.ErrorHandler
import ru.surfstudio.mvi.mappers.handler.ErrorHandlerReducer
import javax.inject.Inject

@Stable
data class BasketState(
    val loadState: LoadStateType = LoadStateType.None
) {
    val uiState: String = when (loadState) {
        LoadStateType.Empty -> "Empty"
        LoadStateType.Error -> "Error"
        LoadStateType.Main -> "Main loading"
        LoadStateType.NoInternet -> "No internet"
        LoadStateType.None -> "None"
        LoadStateType.SwipeRefreshLoading -> "Swipe refresh"
        LoadStateType.TransparentLoading -> "Transparent loading"
    }
}

@ViewModelScoped
class BasketReducer @Inject constructor(
    override val errorHandler: ErrorHandler
) : ErrorHandlerReducer<BasketEvent, BasketState> {

    override fun reduce(state: BasketState, event: BasketEvent): BasketState {
        return when (event) {
            is BasketEvent.DataLoad.GetProductsCountRequest -> state.updateRequestUi(event)
            else -> state
        }
    }

    private fun <T : Any> BasketState.updateRequestUi(event: RequestEvent<T>): BasketState {
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