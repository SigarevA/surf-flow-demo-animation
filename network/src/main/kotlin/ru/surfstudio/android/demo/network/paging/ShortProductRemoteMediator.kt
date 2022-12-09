/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import ru.surfstudio.android.demo.core.Constants
import ru.surfstudio.android.demo.network.generated.interactor.ProductsInteractor
import ru.surfstudio.android.demo.network.room.models.ShortProductModel
import ru.surfstudio.android.demo.network.room.models.toModel
import ru.surfstudio.mvi.mappers.handler.ErrorHandler

@OptIn(ExperimentalPagingApi::class)
class ShortProductRemoteMediator(
    private val productsInteractor: ProductsInteractor,
    private val errorHandler: ErrorHandler,
    private val categoryId: String,
) : RemoteMediator<Int, ShortProductModel>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ShortProductModel>
    ): MediatorResult {
        try {
            when (loadType) {
                LoadType.REFRESH -> {
                    currentOffset = 0
                }
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    currentOffset += Constants.pageSize
                }
            }

            val data = productsInteractor.getProducts(categoryId, currentOffset)
            delay(1500L) //test

            val items = data.products

            productsInteractor.updateProductsCache(
                delete = loadType == LoadType.REFRESH,
                categoryId = categoryId,
                products = items.map { it.toModel(categoryId) }
            )

            return MediatorResult.Success(
                endOfPaginationReached = Constants.pageSize + currentOffset >= data.metadata.count
            )
        } catch (e: Exception) {
            errorHandler.handleError(e)
            return MediatorResult.Error(e)
        }
    }

    companion object {
        private var currentOffset: Int = 0
    }
}