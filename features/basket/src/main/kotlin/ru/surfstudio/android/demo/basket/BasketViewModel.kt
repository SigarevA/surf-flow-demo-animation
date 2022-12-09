/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.basket

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvi.mappers.handler.MviErrorHandlerViewModel
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(
    override val reducer: BasketReducer,
    override val middleware: BasketMiddleware
) : MviErrorHandlerViewModel<BasketState, BasketEvent>() {

    override val state: FlowState<BasketState> = FlowState(BasketState())
    override val hub: FlowEventHub<BasketEvent> = FlowEventHub()

    init {
        bindFlow()
    }
}