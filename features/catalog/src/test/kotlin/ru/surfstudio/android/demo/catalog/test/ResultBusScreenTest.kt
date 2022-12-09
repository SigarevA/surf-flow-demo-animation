/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog.test

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertTrue
import ru.surfstudio.android.demo.catalog.result.*
import ru.surfstudio.android.demo.test_utils.BaseMviScreenTest
import ru.surfstudio.android.demo.test_utils.BaseMviScreenTest.ReducerProducer
import ru.surfstudio.android.demo.test_utils.BaseMviScreenTest.ViewModelProducer
import ru.surfstudio.android.demo.test_utils.TestErrorHandler
import ru.surfstudio.android.demo.test_utils.TestSharedResultBus
import ru.surfstudio.mvi.flow.FlowState

class ResultBusScreenTest : BaseMviScreenTest() {

    private val testParentId = "testParentId"
    private val errorHandler = TestErrorHandler()
    private val sharedResultBus = TestSharedResultBus()
    private val reducerProducer = ReducerProducer { ResultBusReducer(errorHandler) }
    private val viewModelProducer = ViewModelProducer {
        ResultBusViewModel(
            savedStateHandle = SavedStateHandle().apply {
                set("parentId", testParentId)
            },
            middlewareFactory = object : ResultBusMiddleware.Factory {
                override fun create(
                    scope: CoroutineScope,
                    flowState: FlowState<ResultBusState>
                ): ResultBusMiddleware {
                    return ResultBusMiddleware(
                        scope,
                        flowState,
                        sharedResultBus
                    )
                }
            },
            reducer = reducerProducer.produce()
        )
    }

    @Test
    fun `test emit send result`() = runTimeoutTest {
        val viewModel = viewModelProducer.produce()
        viewModel.hub.emit(ResultBusEvent.SendResult)
        assertTrue(sharedResultBus.resultId == testParentId)
    }
}