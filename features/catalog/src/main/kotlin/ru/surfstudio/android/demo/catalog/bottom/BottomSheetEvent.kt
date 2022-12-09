/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.catalog.bottom

import ru.surfstudio.mvi.core.event.Event

sealed class BottomSheetEvent : Event {
    object Action : BottomSheetEvent()
}