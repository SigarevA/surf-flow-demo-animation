/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.basket.test

import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Test
import ru.surfstudio.android.demo.basket.BasketEvent
import ru.surfstudio.android.demo.basket.BasketMiddleware
import ru.surfstudio.android.demo.basket.BasketReducer
import ru.surfstudio.android.demo.basket.BasketViewModel
import ru.surfstudio.android.demo.core.mvi.mapper.LoadStateType
import ru.surfstudio.android.demo.network.generated.interactor.BasketInteractor
import ru.surfstudio.android.demo.test_utils.BaseMviScreenTest
import ru.surfstudio.android.demo.test_utils.BaseMviScreenTest.ReducerProducer
import ru.surfstudio.android.demo.test_utils.TestErrorHandler

class BasketScreenTest : BaseMviScreenTest() {

    private val testBasketCount = 5
    private val succeedBasketInteractor = mockk<BasketInteractor> {
        coEvery { getBasketCount() } returns testBasketCount
    }
    private val errorBasketInteractor = mockk<BasketInteractor> {
        coEvery { getBasketCount() } throws NoSuchElementException()
    }

    private val errorHandler = TestErrorHandler()
    private val reducerProducer = ReducerProducer { BasketReducer(errorHandler) }

    @Test
    fun `test basket reload succeed`() {
        val startEvent = BasketEvent.ReloadClicked
        BasketViewModel(
            reducer = reducerProducer.produce(),
            middleware = BasketMiddleware(testDispatcher, succeedBasketInteractor),
        ).mviScreenTest(
            startEvent = startEvent,
            expectedData = listOf(
                // initial state
                { it.state!!.loadState == LoadStateType.None },
                // start event is emitted
                { it.event == startEvent },
                // loading is triggered
                { (it.event as BasketEvent.DataLoad.GetProductsCountRequest).isLoading },
                // loading state is changed
                { it.state!!.loadState == LoadStateType.Main },
                // data is received
                { (it.event as BasketEvent.DataLoad.GetProductsCountRequest).request.getData() == testBasketCount },
                // loading state is changed to None
                { it.state!!.loadState == LoadStateType.None }
            )
        )
    }

    @Test
    fun `test basket reload error`() {
        val startEvent = BasketEvent.ReloadClicked
        BasketViewModel(
            reducer = reducerProducer.produce(),
            middleware = BasketMiddleware(testDispatcher, errorBasketInteractor),
        ).mviScreenTest(
            startEvent = startEvent,
            expectedData = listOf(
                // initial state
                { it.state!!.loadState == LoadStateType.None },
                // start event is emitted
                { it.event == startEvent },
                // loading is triggered
                { (it.event as BasketEvent.DataLoad.GetProductsCountRequest).isLoading },
                // loading state is changed
                { it.state!!.loadState == LoadStateType.Main },
                // error is received
                { (it.event as BasketEvent.DataLoad.GetProductsCountRequest).request.isError },
                // loading state is changed to Error
                { it.state!!.loadState == LoadStateType.Error }
            )
        )
    }

    /* previous experiments
    @Test
    fun `test basket reload succeed`() {
        val startEvent = BasketEvent.ReloadClicked
        BasketViewModel(
            reducer = reducerProducer.produce(),
            middleware = BasketMiddleware(testDispatcher, succeedBasketInteractor),
        ).mviScreenTest(
            startEvent = startEvent,
            expectedEventsChecks = listOf(
                { it == startEvent },
                { (it as BasketEvent.DataLoad.GetProductsCountRequest).request.isLoading },
                { (it as BasketEvent.DataLoad.GetProductsCountRequest).request.getData() == testBasketCount }
            ),
            expectedStatesChecks = listOf(
                { it.loadState == LoadStateType.None },
                { it.loadState == LoadStateType.Main },
                { it.loadState == LoadStateType.None }
            )
        )
    }*/

    /* previous experiments
    @Test
    fun `test basket reload error`() {
        val startEvent = BasketEvent.ReloadClicked
        BasketViewModel(
            reducer = reducerProducer.produce(),
            middleware = BasketMiddleware(testDispatcher, errorBasketInteractor),
        ).mviScreenTest(
            startEvent = startEvent,
            expectedEventsChecks = listOf(
                { it == startEvent },
                { (it as BasketEvent.DataLoad.GetProductsCountRequest).request.isLoading },
                { (it as BasketEvent.DataLoad.GetProductsCountRequest).request.isError }
            ),
            expectedStatesChecks = listOf(
                { it.loadState == LoadStateType.None },
                { it.loadState == LoadStateType.Main },
                { it.loadState == LoadStateType.Error }
            )
        )
        assertTrue(errorHandler.errorHandled)
    }*/
}