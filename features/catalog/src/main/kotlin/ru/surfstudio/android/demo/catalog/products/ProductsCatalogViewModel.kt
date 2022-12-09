/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog.products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.surfstudio.android.demo.catalog.destinations.ProductsCatalogScreenDestination
import ru.surfstudio.android.demo.core.Constants
import ru.surfstudio.android.demo.domain.entity.products.ShortProductEntity
import ru.surfstudio.android.demo.network.generated.interactor.ProductsInteractor
import ru.surfstudio.android.demo.network.paging.ShortProductRemoteMediator
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvi.mappers.handler.MviErrorHandlerViewModel
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
@HiltViewModel
class ProductsCatalogViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    override val reducer: ProductsCatalogReducer,
    override val middleware: ProductsCatalogMiddleware,
    private val productsInteractor: ProductsInteractor
) : MviErrorHandlerViewModel<ProductsCatalogState, ProductsCatalogEvent>() {

    private val navArgs = ProductsCatalogScreenDestination.argsFrom(savedStateHandle)

    private val categoryId = navArgs.categoryId

    // paging3 is designed for MVVM, we store flow here instead of MVI state
    val products: Flow<PagingData<ShortProductEntity>> = Pager(
        config = PagingConfig(
            pageSize = Constants.pageSize,
            prefetchDistance = 0,
            initialLoadSize = Constants.pageSize
        ),
        remoteMediator = ShortProductRemoteMediator(
            productsInteractor = productsInteractor,
            errorHandler = reducer.errorHandler,
            categoryId = categoryId
        )
    ) {
        //todo mock data for preview/tests
        productsInteractor.getProductsCache(categoryId)
    }.flow.cachedIn(viewModelScope)
        .map { pagingData ->
            pagingData.map {
                it.transform()
            }
        }

    override val state: FlowState<ProductsCatalogState> = FlowState(ProductsCatalogState())
    override val hub: FlowEventHub<ProductsCatalogEvent> = FlowEventHub()

    init {
        bindFlow()
    }
}