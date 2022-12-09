/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.template

import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.mappers.Request
import ru.surfstudio.mvi.mappers.RequestEvent

sealed class ScreenNameEvent : Event {

    sealed class DataLoad<T : Any> : ScreenNameEvent(), RequestEvent<T> {
        data class TemplateRequest(
            override val request: Request<Unit>
        ) : DataLoad<Unit>()
    }
}