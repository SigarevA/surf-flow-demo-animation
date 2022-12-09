/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog.products.details

import androidx.compose.runtime.Stable
import dagger.hilt.android.scopes.ViewModelScoped
import ru.surfstudio.android.demo.core.mvi.mapper.LoadStateType
import ru.surfstudio.android.demo.core.mvi.mapper.RequestMappers
import ru.surfstudio.android.demo.domain.entity.products.DetailedProductEntity
import ru.surfstudio.mvi.mappers.RequestMapper
import ru.surfstudio.mvi.mappers.RequestUi
import ru.surfstudio.mvi.mappers.handler.ErrorHandler
import ru.surfstudio.mvi.mappers.handler.ErrorHandlerReducer
import javax.inject.Inject

@Stable
data class ProductDetailsState(
    val productId: String,
    val requestUi: RequestUi<DetailedProductEntity> = RequestUi()
) {
    val loadState: LoadStateType = requestUi.load as? LoadStateType ?: LoadStateType.Main
    val product: DetailedProductEntity? = requestUi.data
}

@ViewModelScoped
class ProductDetailsReducer @Inject constructor(
    override val errorHandler: ErrorHandler
) : ErrorHandlerReducer<ProductDetailsEvent, ProductDetailsState> {

    override fun reduce(
        state: ProductDetailsState,
        event: ProductDetailsEvent
    ): ProductDetailsState {
        return when (event) {
            is ProductDetailsEvent.DataLoad.ProductDetailsRequest -> state.copy(
                requestUi = RequestMapper
                    .builder(event.request, state.requestUi)
                    .mapData(RequestMappers.data.default())
                    .mapLoading(RequestMappers.loading.default())
                    .handleError(RequestMappers.error.loadingBased(errorHandler))
                    .build()
            )
            else -> state
        }
    }
}