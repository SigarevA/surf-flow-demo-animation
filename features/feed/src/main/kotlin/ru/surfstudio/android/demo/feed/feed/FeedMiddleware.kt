/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.feed.feed

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ru.surfstudio.android.demo.core.shared.result.SharedResultBus
import ru.surfstudio.android.demo.core.snackbar.IconMessageController
import ru.surfstudio.android.demo.domain.entity.products.ShortProductEntity
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvi.mappers.MapperFlowMiddleware

@OptIn(ExperimentalCoroutinesApi::class)
class FeedMiddleware @AssistedInject constructor(
    @Assisted private val flowState: FlowState<FeedState>,
    private val sharedResultBus: SharedResultBus,
    private val messageController: IconMessageController
) : MapperFlowMiddleware<FeedEvent> {

    private val state: FeedState
        get() = flowState.currentState

    override fun transform(eventStream: Flow<FeedEvent>): Flow<FeedEvent> {
        return eventStream.transformations {
            addAll(
                subscribeOnSharedBusScreenResult(),
                subscribeOnBottomSheetResult()
            )
        }
    }

    private fun subscribeOnSharedBusScreenResult(): Flow<FeedEvent> {
        return sharedResultBus.observe<ShortProductEntity>(state.sharedResultScreenParentId)
            .map {
                messageController.show("Shared result bus succeed")
                FeedEvent.OnSharedResultReceived(it)
            }
    }

    private fun subscribeOnBottomSheetResult(): Flow<FeedEvent> {
        return sharedResultBus.observe<Unit>(state.bottomSheetParentId)
            .flatMapLatest {
                messageController.show("Bottom sheet succeed")
                skip()
            }
    }

    @AssistedFactory
    interface Factory {
        fun create(flowState: FlowState<FeedState>): FeedMiddleware
    }
}