/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.surfstudio.android.demo.catalog.destinations.ResultBusScreenDestination
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvi.mappers.handler.MviErrorHandlerViewModel
import javax.inject.Inject

@HiltViewModel
class ResultBusViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    middlewareFactory: ResultBusMiddleware.Factory,
    override val reducer: ResultBusReducer,
) : MviErrorHandlerViewModel<ResultBusState, ResultBusEvent>() {

    private val navArgs = ResultBusScreenDestination.argsFrom(savedStateHandle)

    override val state: FlowState<ResultBusState> = FlowState(ResultBusState(navArgs.parentId))
    override val hub: FlowEventHub<ResultBusEvent> = FlowEventHub()
    override val middleware: ResultBusMiddleware = middlewareFactory.create(viewModelScope, state)

    init {
        bindFlow()
    }
}