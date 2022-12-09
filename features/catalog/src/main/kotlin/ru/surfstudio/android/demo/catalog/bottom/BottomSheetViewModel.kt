/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog.bottom

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.surfstudio.android.demo.catalog.destinations.ResultBusScreenDestination
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvi.mappers.handler.MviErrorHandlerViewModel
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    middlewareFactory: BottomSheetMiddleware.Factory,
    override val reducer: BottomSheetReducer,
) : MviErrorHandlerViewModel<BottomSheetState, BottomSheetEvent>() {

    private val navArgs = ResultBusScreenDestination.argsFrom(savedStateHandle)

    override val state: FlowState<BottomSheetState> = FlowState(BottomSheetState(navArgs.parentId))
    override val hub: FlowEventHub<BottomSheetEvent> = FlowEventHub()
    override val middleware: BottomSheetMiddleware = middlewareFactory.create(viewModelScope, state)

    init {
        bindFlow()
    }
}