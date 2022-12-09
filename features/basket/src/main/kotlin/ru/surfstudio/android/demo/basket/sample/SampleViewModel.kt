/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.basket.sample

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvi.mappers.handler.MviErrorHandlerViewModel
import javax.inject.Inject

@HiltViewModel
class SampleViewModel @Inject constructor(
    override val reducer: SampleReducer,
    override val middleware: SampleMiddleware
) : MviErrorHandlerViewModel<SampleState, SampleEvent>() {

    override val state: FlowState<SampleState> = FlowState(SampleState())
    override val hub: FlowEventHub<SampleEvent> = FlowEventHub()

    init {
        bindFlow()
    }
}