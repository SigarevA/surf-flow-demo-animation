/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog.products

import androidx.compose.runtime.Stable
import dagger.hilt.android.scopes.ViewModelScoped
import ru.surfstudio.mvi.mappers.handler.ErrorHandler
import ru.surfstudio.mvi.mappers.handler.ErrorHandlerReducer
import javax.inject.Inject

// empty MVI entities (state, reducer, middleware) added in case if the screen contains other requests and logic
// which can be normally handled by MVI
@Stable
class ProductsCatalogState

@ViewModelScoped
class ProductsCatalogReducer @Inject constructor(
    override val errorHandler: ErrorHandler
) : ErrorHandlerReducer<ProductsCatalogEvent, ProductsCatalogState> {

    override fun reduce(
        state: ProductsCatalogState,
        event: ProductsCatalogEvent
    ): ProductsCatalogState {
        return when (event) {
            else -> state
        }
    }
}