/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog.result

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.surfstudio.android.demo.core.shared.result.SharedResultBus
import ru.surfstudio.android.demo.core.shared.result.impl.SharedResultImpl
import ru.surfstudio.android.demo.domain.entity.products.ShortProductEntity
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvi.mappers.MapperFlowMiddleware

class ResultBusMiddleware @AssistedInject constructor(
    @Assisted private val scope: CoroutineScope,
    @Assisted private val flowState: FlowState<ResultBusState>,
    private val sharedResultBus: SharedResultBus
) : MapperFlowMiddleware<ResultBusEvent> {

    private val state: ResultBusState
        get() = flowState.currentState

    override fun transform(eventStream: Flow<ResultBusEvent>): Flow<ResultBusEvent> {
        return eventStream.transformations {
            addAll(
                ResultBusEvent.SendResult::class react {
                    scope.launch {
                        sharedResultBus.emit(
                            SharedResultImpl(
                                sharedResultData = ShortProductEntity("test", "test"),
                                sharedResultId = state.parentId
                            )
                        )
                    }
                }
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(scope: CoroutineScope, flowState: FlowState<ResultBusState>): ResultBusMiddleware
    }
}