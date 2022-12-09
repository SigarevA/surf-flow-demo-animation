/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.template

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import ru.surfstudio.android.demo.core.ScreenDispatcher
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvi.mappers.MapperFlowMiddleware

class ScreenNameMiddleware @AssistedInject constructor(
    @Assisted private val scope: CoroutineScope,
    @Assisted private val flowState: FlowState<ScreenNameState>,
    @ScreenDispatcher private val dispatcher: CoroutineDispatcher
) : MapperFlowMiddleware<ScreenNameEvent> {

    private val state: ScreenNameState
        get() = flowState.currentState

    override fun transform(eventStream: Flow<ScreenNameEvent>): Flow<ScreenNameEvent> {
        return eventStream.transformations {
            addAll(
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            scope: CoroutineScope,
            flowState: FlowState<ScreenNameState>
        ): ScreenNameMiddleware
    }
}