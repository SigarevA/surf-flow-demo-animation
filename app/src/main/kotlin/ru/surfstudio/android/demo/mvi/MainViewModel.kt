/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.mvi

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvi.mappers.handler.MviErrorHandlerViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    middlewareFactory: MainMiddleware.Factory,
    override val reducer: MainReducer
) : MviErrorHandlerViewModel<MainState, MainEvent>() {

    override val middleware: MainMiddleware = middlewareFactory.create(viewModelScope)
    override val state: FlowState<MainState> = FlowState(MainState())
    override val hub: FlowEventHub<MainEvent> = FlowEventHub()

    init {
        bindFlow()
    }
}