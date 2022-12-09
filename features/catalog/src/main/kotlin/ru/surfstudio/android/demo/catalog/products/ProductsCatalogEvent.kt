/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog.products

import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.mappers.RequestEvent

sealed class ProductsCatalogEvent : Event {

    sealed class DataLoad<T : Any> : ProductsCatalogEvent(), RequestEvent<T> {

    }
}