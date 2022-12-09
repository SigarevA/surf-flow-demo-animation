/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog.result

import ru.surfstudio.mvi.core.event.Event

sealed class ResultBusEvent : Event {
    object SendResult : ResultBusEvent()
}