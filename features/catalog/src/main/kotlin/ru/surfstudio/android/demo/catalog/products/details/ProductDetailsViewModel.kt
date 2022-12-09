/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog.products.details

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.surfstudio.android.demo.catalog.destinations.ProductDetailsScreenDestination
import ru.surfstudio.mvi.flow.DslFlowMiddleware
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvi.mappers.handler.MviErrorHandlerViewModel
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    middlewareFactory: ProductDetailsMiddleware.Factory,
    override val reducer: ProductDetailsReducer,
) : MviErrorHandlerViewModel<ProductDetailsState, ProductDetailsEvent>() {

    private val navArgs = ProductDetailsScreenDestination.argsFrom(savedStateHandle)

    override val state: FlowState<ProductDetailsState> =
        FlowState(ProductDetailsState(productId = navArgs.productId))

    override val hub: FlowEventHub<ProductDetailsEvent> = FlowEventHub()

    override val middleware: DslFlowMiddleware<ProductDetailsEvent> =
        middlewareFactory.create(state)

    init {
        bindFlow()
    }
}