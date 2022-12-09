/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.template

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvi.mappers.handler.MviErrorHandlerViewModel
import javax.inject.Inject

@HiltViewModel
class ScreenNameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    middlewareFactory: ScreenNameMiddleware.Factory,
    override val reducer: ScreenNameReducer,
) : MviErrorHandlerViewModel<ScreenNameState, ScreenNameEvent>() {

    private val navArgs = ScreenNameScreenDestination.argsFrom(savedStateHandle)

    override val state: FlowState<ScreenNameState> = FlowState(ScreenNameState())
    override val hub: FlowEventHub<ScreenNameEvent> = FlowEventHub()
    override val middleware: ScreenNameMiddleware = middlewareFactory.create(viewModelScope, state)

    init {
        bindFlow()
    }
}