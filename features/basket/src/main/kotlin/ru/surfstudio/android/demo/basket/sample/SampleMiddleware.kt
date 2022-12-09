/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.basket.sample

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.surfstudio.android.demo.basket.sample.SampleEvent.DataLoad.GetProductsCountRequest
import ru.surfstudio.android.demo.core.ScreenDispatcher
import ru.surfstudio.android.demo.core.mvi.mviFlow
import ru.surfstudio.android.demo.network.generated.interactor.BasketInteractor
import ru.surfstudio.mvi.mappers.MapperFlowMiddleware
import javax.inject.Inject

/** Sample screen for unit tests */
@ViewModelScoped
class SampleMiddleware @Inject constructor(
    @ScreenDispatcher private val dispatcher: CoroutineDispatcher,
    private val basketInteractor: BasketInteractor,
) : MapperFlowMiddleware<SampleEvent> {

    override fun transform(eventStream: Flow<SampleEvent>): Flow<SampleEvent> {
        return eventStream.transformations {
            addAll(
                // event -> request
                SampleEvent.TriggerLoadingTest::class eventToStream { getBasketCount() },

                // event = event + request
                SampleEvent.ReloadClicked::class eventToEvent { SampleEvent.AdditionalMappingTest },
                SampleEvent.ReloadClicked::class eventToStream { getBasketCount() },

                // event -> merge(event, request)
                SampleEvent.ReloadMergeTest::class eventToStream {
                    merge(
                        flowOf(SampleEvent.AdditionalMappingMergeTest),
                        getBasketCount()
                    )
                },

                // event -> another event -> request
                SampleEvent.SampleChain::class eventToEvent { SampleEvent.SampleChainNext },
                SampleEvent.SampleChainNext::class eventToStream { getBasketCount() }
            )
        }
    }

    private fun getBasketCount(): Flow<SampleEvent> =
        mviFlow(dispatcher) { basketInteractor.getBasketCount() }
            .asRequestEvent(::GetProductsCountRequest)
}