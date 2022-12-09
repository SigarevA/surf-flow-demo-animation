/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.basket.test

import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Test
import ru.surfstudio.android.demo.basket.sample.SampleEvent
import ru.surfstudio.android.demo.basket.sample.SampleMiddleware
import ru.surfstudio.android.demo.basket.sample.SampleReducer
import ru.surfstudio.android.demo.basket.sample.SampleViewModel
import ru.surfstudio.android.demo.core.mvi.mapper.LoadStateType
import ru.surfstudio.android.demo.network.generated.interactor.BasketInteractor
import ru.surfstudio.android.demo.test_utils.BaseMviScreenTest
import ru.surfstudio.android.demo.test_utils.BaseMviScreenTest.MiddlewareProducer
import ru.surfstudio.android.demo.test_utils.BaseMviScreenTest.ReducerProducer
import ru.surfstudio.android.demo.test_utils.TestErrorHandler

class SampleScreenTest : BaseMviScreenTest() {

    private val testBasketCount = 5
    private val basketInteractor = mockk<BasketInteractor> {
        coEvery { getBasketCount() } returns testBasketCount
    }

    private val reducerProducer = ReducerProducer { SampleReducer(TestErrorHandler()) }
    private val middlewareProducer = MiddlewareProducer {
        SampleMiddleware(testDispatcher, basketInteractor)
    }

    //region simple
    @Test
    fun `test simple request event mappings and state updates`() {
        val startEvent = SampleEvent.TriggerLoadingTest
        SampleViewModel(
            reducer = reducerProducer.produce(),
            middleware = middlewareProducer.produce()
        ).mviScreenTest(
            startEvent = startEvent,
            expectedData = listOf(
                // initial state
                { it.state!!.loadState == LoadStateType.None },
                // start event is emitted
                { it.event == startEvent },
                // loading is triggered
                { (it.event as SampleEvent.DataLoad.GetProductsCountRequest).request.isLoading },
                // loading state is changed
                { it.state!!.loadState == LoadStateType.Main },
                // data is received
                { (it.event as SampleEvent.DataLoad.GetProductsCountRequest).request.getData() == testBasketCount },
                // loading state is changed to None
                { it.state!!.loadState == LoadStateType.None }
            )
        )
    }

    @Test
    fun `test simple request event mappings only`() {
        val startEvent = SampleEvent.TriggerLoadingTest
        SampleViewModel(
            reducer = reducerProducer.produce(),
            middleware = middlewareProducer.produce()
        ).hub.middlewareTest(
            startEvent = startEvent,
            expectedEventsChecks = listOf(
                { it == startEvent },
                { (it as SampleEvent.DataLoad.GetProductsCountRequest).request.isLoading },
                { (it as SampleEvent.DataLoad.GetProductsCountRequest).request.getData() == testBasketCount }
            )
        )
    }

    @Test
    fun `test simple request state updates only`() {
        checkReducerLoadStateUpdates(startEvent = SampleEvent.TriggerLoadingTest)
    }
    //endregion

    //region advanced
    @Test
    fun `test advanced request event mappings and state updates`() {
        checkMviScreenChainMappings(
            startEvent = SampleEvent.ReloadClicked,
            nextEvent = SampleEvent.AdditionalMappingTest
        )
    }

    @Test
    fun `test advanced request event mappings only`() {
        checkMiddlewareRequestMappings(
            startEvent = SampleEvent.ReloadClicked,
            nextEvent = SampleEvent.AdditionalMappingTest
        )
    }

    @Test
    fun `test advanced request state updates only`() {
        checkReducerLoadStateUpdates(startEvent = SampleEvent.ReloadClicked)
    }
    //endregion

    //region merge
    @Test
    fun `test merge request event mappings and state updates`() {
        checkMviScreenChainMappings(
            startEvent = SampleEvent.ReloadMergeTest,
            nextEvent = SampleEvent.AdditionalMappingMergeTest
        )
    }

    @Test
    fun `test merge request event mappings only`() {
        checkMiddlewareRequestMappings(
            startEvent = SampleEvent.ReloadMergeTest,
            nextEvent = SampleEvent.AdditionalMappingMergeTest
        )
    }

    @Test
    fun `test merge request state updates only`() {
        checkReducerLoadStateUpdates(startEvent = SampleEvent.ReloadMergeTest)
    }
    //endregion

    //region chain
    @Test
    fun `test chain request event mappings and state updates`() {
        checkMviScreenChainMappings(
            startEvent = SampleEvent.SampleChain,
            nextEvent = SampleEvent.SampleChainNext
        )
    }

    @Test
    fun `test chain request event mappings only`() {
        checkMiddlewareRequestMappings(
            startEvent = SampleEvent.SampleChain,
            nextEvent = SampleEvent.SampleChainNext
        )
    }

    @Test
    fun `test chain request state updates only`() {
        checkReducerLoadStateUpdates(startEvent = SampleEvent.SampleChain)
    }
    //endregion

    private fun checkMviScreenChainMappings(startEvent: SampleEvent, nextEvent: SampleEvent) {
        SampleViewModel(
            reducer = reducerProducer.produce(),
            middleware = middlewareProducer.produce()
        ).mviScreenTest(
            startEvent = startEvent,
            expectedData = listOf(
                // initial state
                { it.state!!.loadState == LoadStateType.None },
                // start event is emitted
                { it.event == startEvent },
                // next event is emitted
                { it.event == nextEvent },
                // loading is triggered
                { (it.event as SampleEvent.DataLoad.GetProductsCountRequest).request.isLoading },
                // loading state is changed
                { it.state!!.loadState == LoadStateType.Main },
                // data is received
                { (it.event as SampleEvent.DataLoad.GetProductsCountRequest).request.getData() == testBasketCount },
                // loading state is changed to None
                { it.state!!.loadState == LoadStateType.None }
            )
        )
    }

    private fun checkMiddlewareRequestMappings(startEvent: SampleEvent, nextEvent: SampleEvent) {
        SampleViewModel(
            reducer = reducerProducer.produce(),
            middleware = middlewareProducer.produce()
        ).hub.middlewareTest(
            startEvent = startEvent,
            expectedEventsChecks = listOf(
                { it == startEvent },
                { it == nextEvent },
                { (it as SampleEvent.DataLoad.GetProductsCountRequest).request.isLoading },
                { (it as SampleEvent.DataLoad.GetProductsCountRequest).request.getData() == testBasketCount }
            )
        )
    }

    private fun checkReducerLoadStateUpdates(startEvent: SampleEvent) {
        SampleViewModel(
            reducer = reducerProducer.produce(),
            middleware = middlewareProducer.produce()
        ).reducerTest(
            startEvent = startEvent,
            expectedStatesChecks = listOf(
                { it.loadState == LoadStateType.None },
                { it.loadState == LoadStateType.Main },
                { it.loadState == LoadStateType.None }
            )
        )
    }
}