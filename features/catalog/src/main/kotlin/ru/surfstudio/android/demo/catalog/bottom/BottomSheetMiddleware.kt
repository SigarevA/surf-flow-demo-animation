/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog.bottom

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.surfstudio.android.demo.core.shared.result.SharedResultBus
import ru.surfstudio.android.demo.core.shared.result.impl.SharedResultImpl
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvi.mappers.MapperFlowMiddleware

class BottomSheetMiddleware @AssistedInject constructor(
    @Assisted private val scope: CoroutineScope,
    @Assisted private val flowState: FlowState<BottomSheetState>,
    private val sharedResultBus: SharedResultBus
) : MapperFlowMiddleware<BottomSheetEvent> {

    private val state: BottomSheetState
        get() = flowState.currentState

    override fun transform(eventStream: Flow<BottomSheetEvent>): Flow<BottomSheetEvent> {
        return eventStream.transformations {
            addAll(
                BottomSheetEvent.Action::class react {
                    scope.launch {
                        sharedResultBus.emit(
                            SharedResultImpl(
                                sharedResultData = Unit,
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
        fun create(
            scope: CoroutineScope,
            flowState: FlowState<BottomSheetState>
        ): BottomSheetMiddleware
    }
}