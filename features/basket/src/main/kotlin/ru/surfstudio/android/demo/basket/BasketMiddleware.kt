/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.basket

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.surfstudio.android.demo.basket.BasketEvent.DataLoad.GetProductsCountRequest
import ru.surfstudio.android.demo.core.ScreenDispatcher
import ru.surfstudio.android.demo.core.mvi.mviFlow
import ru.surfstudio.android.demo.network.generated.interactor.BasketInteractor
import ru.surfstudio.mvi.mappers.MapperFlowMiddleware
import javax.inject.Inject

@ViewModelScoped
class BasketMiddleware @Inject constructor(
    @ScreenDispatcher private val dispatcher: CoroutineDispatcher,
    private val basketInteractor: BasketInteractor,
) : MapperFlowMiddleware<BasketEvent> {

    override fun transform(eventStream: Flow<BasketEvent>): Flow<BasketEvent> {
        return eventStream.transformations {
            addAll(
                BasketEvent.ReloadClicked::class eventToStream { getBasketCount() }
            )
        }
    }

    private fun getBasketCount(): Flow<BasketEvent> =
        mviFlow(dispatcher) { basketInteractor.getBasketCount() }
            .asRequestEvent(::GetProductsCountRequest)
}