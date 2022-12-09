/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.mvi

import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.mappers.Request
import ru.surfstudio.mvi.mappers.RequestEvent

sealed class MainEvent : Event {
    object CloseSnack : MainEvent()

    data class GetProductsCountRequest(
        override val request: Request<Int>
    ) : RequestEvent<Int>, MainEvent()
}