/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.feed.feed

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvi.mappers.handler.MviErrorHandlerViewModel
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    middlewareFactory: FeedMiddleware.Factory,
    override val reducer: FeedReducer
) : MviErrorHandlerViewModel<FeedState, FeedEvent>() {

    override val state: FlowState<FeedState> = FlowState(FeedState())
    override val hub: FlowEventHub<FeedEvent> = FlowEventHub()
    override val middleware: FeedMiddleware = middlewareFactory.create(state)

    init {
        bindFlow()
    }
}