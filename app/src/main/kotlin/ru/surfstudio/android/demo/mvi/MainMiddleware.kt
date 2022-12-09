/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.mvi

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.surfstudio.android.demo.core.ScreenDispatcher
import ru.surfstudio.android.demo.core.ScreenLoadOnStart
import ru.surfstudio.android.demo.core.mvi.mviFlow
import ru.surfstudio.android.demo.core.snackbar.IconMessageController
import ru.surfstudio.android.demo.mvi.MainEvent.GetProductsCountRequest
import ru.surfstudio.android.demo.network.generated.interactor.BasketInteractor
import ru.surfstudio.android.demo.network.session.SessionChangedInteractor
import ru.surfstudio.mvi.mappers.MapperFlowMiddleware

class MainMiddleware @AssistedInject constructor(
    @Assisted private val scope: CoroutineScope,
    @ScreenDispatcher private val dispatcher: CoroutineDispatcher,
    @ScreenLoadOnStart private val loadOnStart: Boolean,
    private val basketInteractor: BasketInteractor,
    private val sessionChangedInteractor: SessionChangedInteractor,
    private val messageController: IconMessageController
) : MapperFlowMiddleware<MainEvent> {

    init {
        scope.launch {
            sessionChangedInteractor.provideSavedTokens()
        }
    }

    override fun transform(eventStream: Flow<MainEvent>): Flow<MainEvent> {
        return eventStream.transformations {
            addAll(
                if (loadOnStart) getBasketCount() else skip(),
                MainEvent.CloseSnack::class react { messageController.closeSnack() }
            )
        }
    }

    private fun getBasketCount(): Flow<MainEvent> =
        mviFlow(dispatcher) { basketInteractor.getBasketCount() }
            .asRequestEvent(::GetProductsCountRequest)

    @AssistedFactory
    interface Factory {
        fun create(scope: CoroutineScope): MainMiddleware
    }
}