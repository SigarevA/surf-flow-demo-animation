/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog.products.details

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import ru.surfstudio.android.demo.catalog.products.details.ProductDetailsEvent.DataLoad.ProductDetailsRequest
import ru.surfstudio.android.demo.core.ScreenDispatcher
import ru.surfstudio.android.demo.core.ScreenLoadOnStart
import ru.surfstudio.android.demo.network.generated.interactor.ProductsInteractor
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvi.mappers.MapperFlowMiddleware

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailsMiddleware @AssistedInject constructor(
    @Assisted private val flowState: FlowState<ProductDetailsState>,
    @ScreenDispatcher private val dispatcher: CoroutineDispatcher,
    @ScreenLoadOnStart private val loadOnStart: Boolean,
    private val productsInteractor: ProductsInteractor
) : MapperFlowMiddleware<ProductDetailsEvent> {

    private val state: ProductDetailsState
        get() = flowState.currentState

    override fun transform(eventStream: Flow<ProductDetailsEvent>): Flow<ProductDetailsEvent> {
        return eventStream.transformations {
            addAll(
                if (loadOnStart) getProductDetails() else skip(),
                ProductDetailsEvent.Retry::class eventToStream { getProductDetails() }
            )
        }
    }

    private fun getProductDetails(): Flow<ProductDetailsEvent> =
        productsInteractor.getProductDetails(state.productId, dispatcher = dispatcher)
            .asRequestEvent(::ProductDetailsRequest)

    @AssistedFactory
    interface Factory {
        fun create(flowState: FlowState<ProductDetailsState>): ProductDetailsMiddleware
    }
}