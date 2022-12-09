/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog.products.details

import ru.surfstudio.android.demo.domain.entity.products.DetailedProductEntity
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.mappers.Request
import ru.surfstudio.mvi.mappers.RequestEvent

sealed class ProductDetailsEvent : Event {

    object Retry : ProductDetailsEvent()

    sealed class DataLoad<T : Any> : ProductDetailsEvent(), RequestEvent<T> {
        data class ProductDetailsRequest(
            override val request: Request<DetailedProductEntity>
        ) : DataLoad<DetailedProductEntity>()
    }
}