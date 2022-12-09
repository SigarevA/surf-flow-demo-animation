/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog.test

import androidx.lifecycle.SavedStateHandle
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import ru.surfstudio.android.demo.catalog.products.details.*
import ru.surfstudio.android.demo.catalog.test.ProductDetailsScreenTest.ViewModelProducer
import ru.surfstudio.android.demo.core.mvi.mapper.LoadStateType
import ru.surfstudio.android.demo.core.network.NoInternetException
import ru.surfstudio.android.demo.domain.entity.products.DetailedProductEntity
import ru.surfstudio.android.demo.network.generated.interactor.ProductsInteractor
import ru.surfstudio.android.demo.test_utils.BaseMviScreenTest
import ru.surfstudio.android.demo.test_utils.BaseMviScreenTest.ReducerProducer
import ru.surfstudio.android.demo.test_utils.TestErrorHandler
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvi.mappers.Request
import ru.surfstudio.mvi.mappers.handler.MviErrorHandlerViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailsScreenTest : BaseMviScreenTest() {

    private val testProductId = "test"
    private val testProductDetails = DetailedProductEntity()
    private val succeedProductsInteractor = mockk<ProductsInteractor> {
        every {
            getProductDetails(
                productId = testProductId,
                fromCache = any(),
                dispatcher = testDispatcher
            )
        } returns flowOf(testProductDetails).flowOn(testDispatcher)
    }
    private val errorProductsInteractor = mockk<ProductsInteractor> {
        every {
            getProductDetails(
                productId = testProductId,
                fromCache = any(),
                dispatcher = testDispatcher
            )
        } returns flow<DetailedProductEntity> {
            throw NoSuchElementException()
        }.flowOn(testDispatcher)
    }
    private val noInternetProductsInteractor = mockk<ProductsInteractor> {
        every {
            getProductDetails(
                productId = testProductId,
                fromCache = any(),
                dispatcher = testDispatcher
            )
        } returns flow<DetailedProductEntity> {
            throw NoInternetException(RuntimeException())
        }.flowOn(testDispatcher)
    }

    private val errorHandler = TestErrorHandler()
    private val reducerProducer = ReducerProducer { ProductDetailsReducer(errorHandler) }
    private val viewModelProducer = ViewModelProducer { interactor, loadOnStart ->
        ProductDetailsViewModel(
            savedStateHandle = SavedStateHandle().apply {
                set("productId", testProductId)
            },
            middlewareFactory = object : ProductDetailsMiddleware.Factory {
                override fun create(flowState: FlowState<ProductDetailsState>): ProductDetailsMiddleware {
                    return ProductDetailsMiddleware(
                        flowState,
                        testDispatcher,
                        loadOnStart = loadOnStart,
                        productsInteractor = interactor
                    )
                }
            },
            reducer = reducerProducer.produce()
        )
    }

    //region succeed
    @Test
    fun `test product details reload succeed with no initial data`() {
        val startEvent = ProductDetailsEvent.Retry
        viewModelProducer.produce(succeedProductsInteractor, loadOnStart = false).mviScreenTest(
            startEvent = startEvent,
            expectedData = listOf(
                // initial state
                { it.state!!.requestUi.isEmpty && it.state!!.product == null },
                // start event is emitted
                { it.event == startEvent },
                // loading is triggered
                { (it.event as ProductDetailsEvent.DataLoad.ProductDetailsRequest).isLoading },
                // loading state is changed
                { it.state!!.loadState == LoadStateType.Main },
                // data is received
                { (it.event as ProductDetailsEvent.DataLoad.ProductDetailsRequest).request.getData() == testProductDetails },
                // loading state is changed to None
                { it.state!!.loadState == LoadStateType.None }
            )
        )
    }
    //endregion

    //region error
    @Test
    fun `test product details reload error with no initial data`() {
        val startEvent = ProductDetailsEvent.Retry
        viewModelProducer.produce(errorProductsInteractor, loadOnStart = false).mviScreenTest(
            startEvent = startEvent,
            expectedData = listOf(
                // initial state
                { it.state!!.requestUi.isEmpty && it.state!!.product == null },
                // start event is emitted
                { it.event == startEvent },
                // loading is triggered
                { (it.event as ProductDetailsEvent.DataLoad.ProductDetailsRequest).isLoading },
                // loading state is changed
                { it.state!!.loadState == LoadStateType.Main },
                // error is received
                { (it.event as ProductDetailsEvent.DataLoad.ProductDetailsRequest).request.isError },
                // loading state is changed to Error
                { it.state!!.loadState == LoadStateType.Error }
            )
        )
        assertFalse(errorHandler.errorHandled)
    }

    @Test
    fun `test product details reload error with initial data`() = runTimeoutTest {
        testErrorWithInitialData(errorProductsInteractor)
    }
    //endregion

    //region no internet
    @Test
    fun `test product details reload no internet with no initial data`() {
        val startEvent = ProductDetailsEvent.Retry
        viewModelProducer.produce(noInternetProductsInteractor, loadOnStart = false).mviScreenTest(
            startEvent = startEvent,
            expectedData = listOf(
                // initial state
                { it.state!!.requestUi.isEmpty && it.state!!.product == null },
                // start event is emitted
                { it.event == startEvent },
                // loading is triggered
                { (it.event as ProductDetailsEvent.DataLoad.ProductDetailsRequest).isLoading },
                // loading state is changed
                { it.state!!.loadState == LoadStateType.Main },
                // error is received
                { (it.event as ProductDetailsEvent.DataLoad.ProductDetailsRequest).request.isError },
                // loading state is changed to NoInternet
                { it.state!!.loadState == LoadStateType.NoInternet }
            )
        )
        assertFalse(errorHandler.errorHandled)
    }

    @Test
    fun `test product details no internet error with initial data`() = runTimeoutTest {
        testErrorWithInitialData(noInternetProductsInteractor)
    }
    //endregion

    /* previous experiments.
    different test behavior for flow vs suspend fun on start
    @Test
    fun `test product details request on screen start`() {
        // all events are emitted during middleware creation, after observing we can see only result
        viewModelProducer.produce(succeedProductsInteractor, loadOnStart = true).mviScreenTest(
            startEvent = null,
            expectedData = listOf(
                { it.state!!.loadState == LoadStateType.None && it.state!!.product == testProductDetails }
            )
        )
    }*/

    private fun testErrorWithInitialData(interactor: ProductsInteractor) = runTimeoutTest {
        val viewModel = viewModelProducer.produce(interactor, loadOnStart = false)
        // initialize succeed data for previous state
        viewModel.hub.emit(
            ProductDetailsEvent.DataLoad.ProductDetailsRequest(
                Request.Success(testProductDetails)
            )
        )
        val startEvent = ProductDetailsEvent.Retry
        viewModel.mviScreenTest(
            startEvent = startEvent,
            expectedData = listOf(
                // initial state
                { it.state!!.product == testProductDetails },
                // start event is emitted
                { it.event == startEvent },
                // loading is triggered
                { (it.event as ProductDetailsEvent.DataLoad.ProductDetailsRequest).isLoading },
                // loading state is changed
                { it.state!!.loadState == LoadStateType.TransparentLoading },
                // error is received
                { (it.event as ProductDetailsEvent.DataLoad.ProductDetailsRequest).request.isError },
                // loading state is changed to Error
                { it.state!!.loadState == LoadStateType.None && it.state!!.product == testProductDetails }
            )
        )
        assertTrue(errorHandler.errorHandled)
    }

    /* previous experiments
    @Test
    fun `test product details reload succeed`() {
        val startEvent = ProductDetailsEvent.Retry
        viewModel.produce(succeedProductsInteractor).mviScreenTest(
            startEvent = startEvent,
            expectedEventsChecks = listOf(
                { it == startEvent },
                { (it as ProductDetailsEvent.DataLoad.ProductDetailsRequest).request.isLoading },
                { (it as ProductDetailsEvent.DataLoad.ProductDetailsRequest).request.isSuccess },
            ),
            expectedStatesChecks = listOf(
                { it.requestUi.isEmpty && it.product == null },
                { it.loadState == LoadStateType.Main },
                { it.loadState == LoadStateType.None }
            )
        )
    }*/

    /*
    @Test
    fun `test product details reload error`() {
        val startEvent = ProductDetailsEvent.Retry
        viewModel.produce(errorProductsInteractor).mviScreenTest(
            startEvent = startEvent,
            expectedEventsChecks = listOf(
                { it == startEvent },
                { (it as ProductDetailsEvent.DataLoad.ProductDetailsRequest).request.isLoading },
                { (it as ProductDetailsEvent.DataLoad.ProductDetailsRequest).request.isError }
            ),
            expectedStatesChecks = listOf(
                { it.requestUi.isEmpty && it.product == null },
                { it.loadState == LoadStateType.Main },
                { it.loadState == LoadStateType.Error }
            )
        )
        assertFalse(errorHandler.errorHandled)
    } */

    private fun interface ViewModelProducer<SH : Any, E : Event, VM : MviErrorHandlerViewModel<SH, E>> {
        fun produce(interactor: ProductsInteractor, loadOnStart: Boolean): VM
    }
}