/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.basket

import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.mappers.Request
import ru.surfstudio.mvi.mappers.RequestEvent

sealed class BasketEvent : Event {
    object ReloadClicked : BasketEvent()

    sealed class DataLoad<T : Any> : BasketEvent(), RequestEvent<T> {
        data class GetProductsCountRequest(
            override val request: Request<Int>
        ) : DataLoad<Int>()
    }
}