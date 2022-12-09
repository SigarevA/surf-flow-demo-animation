/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.test

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import ru.surfstudio.android.demo.mvi.MainEvent
import ru.surfstudio.android.demo.mvi.MainMiddleware
import ru.surfstudio.android.demo.mvi.MainReducer
import ru.surfstudio.android.demo.mvi.MainViewModel
import ru.surfstudio.android.demo.network.generated.interactor.BasketInteractor
import ru.surfstudio.android.demo.test.MainScreenTest.ViewModelProducer
import ru.surfstudio.android.demo.test_utils.BaseMviScreenTest
import ru.surfstudio.android.demo.test_utils.BaseMviScreenTest.ReducerProducer
import ru.surfstudio.android.demo.test_utils.TestErrorHandler
import ru.surfstudio.android.demo.test_utils.TestMessageController
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.mappers.handler.MviErrorHandlerViewModel

class MainScreenTest : BaseMviScreenTest() {

    private val testBasketCount = 5
    private val succeedBasketInteractor = mockk<BasketInteractor> {
        coEvery { getBasketCount() } returns testBasketCount
    }

    private val errorHandler = TestErrorHandler()
    private val messageController = TestMessageController()
    private val reducerProducer = ReducerProducer { MainReducer(errorHandler) }
    private val viewModelProducer = ViewModelProducer { interactor, loadOnStart ->
        MainViewModel(
            middlewareFactory = object : MainMiddleware.Factory {
                override fun create(scope: CoroutineScope): MainMiddleware {
                    return MainMiddleware(
                        scope,
                        testDispatcher,
                        loadOnStart = loadOnStart,
                        basketInteractor = interactor,
                        sessionChangedInteractor = mockk {
                            coEvery { provideSavedTokens() } returns Unit
                        },
                        messageController = messageController
                    )
                }
            },
            reducer = reducerProducer.produce()
        )
    }

    @Test
    fun `test close snack command`() = runTimeoutTest {
        // initial state
        messageController.show("")
        assertTrue(messageController.snackIsShown)

        val viewModel = viewModelProducer.produce(
            succeedBasketInteractor,
            loadOnStart = false
        )
        viewModel.hub.emit(MainEvent.CloseSnack)
        assertFalse(messageController.snackIsShown)
    }

    private fun interface ViewModelProducer<SH : Any, E : Event, VM : MviErrorHandlerViewModel<SH, E>> {
        fun produce(interactor: BasketInteractor, loadOnStart: Boolean): VM
    }
}