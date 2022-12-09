/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.feed.test

import app.cash.turbine.test
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertTrue
import ru.surfstudio.android.demo.core.shared.result.impl.SharedResultBusImpl
import ru.surfstudio.android.demo.core.shared.result.impl.SharedResultImpl
import ru.surfstudio.android.demo.domain.entity.products.ShortProductEntity
import ru.surfstudio.android.demo.feed.feed.*
import ru.surfstudio.android.demo.test_utils.BaseMviScreenTest
import ru.surfstudio.android.demo.test_utils.BaseMviScreenTest.ReducerProducer
import ru.surfstudio.android.demo.test_utils.BaseMviScreenTest.ViewModelProducer
import ru.surfstudio.android.demo.test_utils.TestErrorHandler
import ru.surfstudio.android.demo.test_utils.TestMessageController
import ru.surfstudio.mvi.flow.FlowState

class FeedScreenTest : BaseMviScreenTest() {

    private val testResultData = ShortProductEntity()
    private val errorHandler = TestErrorHandler()
    private val sharedResultBus = SharedResultBusImpl()
    private val messageController = TestMessageController()
    private val reducerProducer = ReducerProducer { FeedReducer(errorHandler) }
    private val viewModelProducer = ViewModelProducer {
        FeedViewModel(
            middlewareFactory = object : FeedMiddleware.Factory {
                override fun create(flowState: FlowState<FeedState>): FeedMiddleware {
                    return FeedMiddleware(
                        flowState,
                        sharedResultBus,
                        messageController
                    )
                }
            },
            reducer = reducerProducer.produce()
        )
    }

    @Test
    fun `test subscribeOnSharedBusScreenResult`() = runTimeoutTest {
        val viewModel = viewModelProducer.produce()
        val sharedResultId = viewModel.state.currentState.sharedResultScreenParentId
        viewModel.hub.observe().test {
            sharedResultBus.emit(
                SharedResultImpl(
                    sharedResultData = testResultData,
                    sharedResultId = sharedResultId
                )
            )
            val event = awaitItem()
            assertTrue((event as FeedEvent.OnSharedResultReceived).data == testResultData)
            assertTrue(messageController.snackIsShown)
        }
    }

    @Test
    fun `test subscribeOnBottomSheetResult`() = runTimeoutTest {
        val viewModel = viewModelProducer.produce()
        val sharedResultId = viewModel.state.currentState.bottomSheetParentId
        viewModel.hub.observe().test {
            sharedResultBus.emit(
                SharedResultImpl(
                    sharedResultData = Unit,
                    sharedResultId = sharedResultId
                )
            )
            assertTrue(messageController.snackIsShown)
        }
    }
}