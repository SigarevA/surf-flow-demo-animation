/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.feed.feed

import ru.surfstudio.android.demo.domain.entity.products.ShortProductEntity
import ru.surfstudio.mvi.core.event.Event

sealed class FeedEvent : Event {

    data class OnSharedResultReceived(val data: ShortProductEntity) : FeedEvent()
    data class OnDialogStateChanged(val show: Boolean) : FeedEvent()
}