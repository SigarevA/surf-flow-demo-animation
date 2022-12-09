/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.basket.sample

import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.mappers.Request
import ru.surfstudio.mvi.mappers.RequestEvent

/** Sample screen for unit tests */
sealed class SampleEvent : Event {
    object ReloadMergeTest : SampleEvent()
    object AdditionalMappingMergeTest : SampleEvent()

    object ReloadClicked : SampleEvent()
    object AdditionalMappingTest : SampleEvent()

    object TriggerLoadingTest : SampleEvent()

    object SampleChain : SampleEvent()
    object SampleChainNext : SampleEvent()

    sealed class DataLoad<T : Any> : SampleEvent(), RequestEvent<T> {
        data class GetProductsCountRequest(
            override val request: Request<Int>
        ) : DataLoad<Int>()
    }
}