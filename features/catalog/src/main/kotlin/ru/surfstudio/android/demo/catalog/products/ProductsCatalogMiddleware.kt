/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog.products

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import ru.surfstudio.mvi.mappers.MapperFlowMiddleware
import javax.inject.Inject

@ViewModelScoped
class ProductsCatalogMiddleware @Inject constructor(
) : MapperFlowMiddleware<ProductsCatalogEvent> {

    override fun transform(eventStream: Flow<ProductsCatalogEvent>): Flow<ProductsCatalogEvent> {
        return eventStream.transformations {
            addAll(

            )
        }
    }
}