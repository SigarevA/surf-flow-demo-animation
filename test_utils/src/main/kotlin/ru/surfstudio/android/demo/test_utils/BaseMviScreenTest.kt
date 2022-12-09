/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.test_utils

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge
import org.junit.jupiter.api.Assertions.assertTrue
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.core.reducer.Reducer
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.mappers.MapperFlowMiddleware
import ru.surfstudio.mvi.mappers.handler.MviErrorHandlerViewModel

/** Base class for MVI screens unit tests */
@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseMviScreenTest : BaseUnitTest() {

    /** Test `Middleware` and `Reducer` together */
    protected fun <E : Event, S : Any> MviErrorHandlerViewModel<S, E>.mviScreenTest(
        startEvent: E?,
        expectedData: List<(MviData<S, E>) -> Boolean>
    ) {
        mviScreenTestImpl({ startEvent?.let { hub.emit(startEvent) } }, expectedData)
    }

    /** Test `Middleware` and `Reducer` together */
    protected fun <E : Event, S : Any> MviErrorHandlerViewModel<S, E>.mviScreenTest(
        startAction: suspend () -> Unit,
        expectedData: List<(MviData<S, E>) -> Boolean>
    ) {
        mviScreenTestImpl(startAction, expectedData)
    }

    /** Test `Middleware` event mappings */
    protected fun <E : Event> FlowEventHub<E>.middlewareTest(
        startEvent: E?,
        expectedEventsChecks: List<(E) -> Boolean>
    ) {
        runTimeoutTest {
            observe().test {
                startEvent?.let { emit(startEvent) }
                (expectedEventsChecks.indices).forEach { index ->
                    val event = awaitItem()
                    println("current event: $event")
                    assertTrue(expectedEventsChecks[index](event))
                }
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    /** Test `Middleware` event mappings until expected */
    protected fun <E : Event> FlowEventHub<E>.middlewareTest(
        startEvents: List<E>?,
        expectedFinalEventCheck: (E) -> Boolean
    ) {
        runTimeoutTest {
            observe().test {
                startEvents?.let { startEvents.forEach { emit(it) } }
                while (true) {
                    val event = awaitItem()
                    println("current event: $event")
                    if (expectedFinalEventCheck(event)) {
                        println("expectedFinalEventCheck is succeed")
                        cancelAndIgnoreRemainingEvents()
                        break
                    }
                }
            }
        }
    }

    /** Test `Reducer` state updates */
    protected fun <E : Event, S : Any> MviErrorHandlerViewModel<S, E>.reducerTest(
        startEvent: E?,
        expectedStatesChecks: List<(S) -> Boolean>
    ) {
        runTimeoutTest {
            state.observeState().test {
                startEvent?.let { hub.emit(startEvent) }
                (expectedStatesChecks.indices).forEach { index ->
                    val state = awaitItem()
                    println("current state: $state")
                    assertTrue(expectedStatesChecks[index](state))
                }
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    /** Test `Reducer` state updates until expected */
    protected fun <E : Event, S : Any> MviErrorHandlerViewModel<S, E>.reducerTest(
        startEvents: List<E>?,
        expectedFinalStateCheck: (S) -> Boolean
    ) {
        runTimeoutTest {
            state.observeState().test {
                startEvents?.let { startEvents.forEach { hub.emit(it) } }
                while (true) {
                    val state = awaitItem()
                    println("current state: $state")
                    if (expectedFinalStateCheck(state)) {
                        println("expectedFinalStateCheck is succeed")
                        cancelAndIgnoreRemainingEvents()
                        break
                    }
                }
            }
        }
    }

    private fun <E : Event, S : Any> MviErrorHandlerViewModel<S, E>.mviScreenTestImpl(
        startAction: suspend () -> Unit,
        expectedData: List<(MviData<S, E>) -> Boolean>
    ) {
        runTimeoutTest {
            merge(
                hub.observe().flatMapLatest { flowOf(MviData<S, E>(event = it)) },
                state.observeState().flatMapLatest { flowOf(MviData(state = it)) },
            ).test {
                startAction()
                (expectedData.indices).forEach { index ->
                    val item = awaitItem()
                    item.event?.let {
                        println("current event: $it")
                    }
                    item.state?.let {
                        println("current state: $it")
                    }
                    assertTrue(expectedData[index](item))
                }
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    /* previous experiments
    /** Test `Middleware` and `Reducer` together */
    protected fun <E : Event, S : Any> MviErrorHandlerViewModel<S, E>.mviScreenTest(
        startEvent: E?,
        expectedEventsChecks: List<(E) -> Boolean>,
        expectedStatesChecks: List<(S) -> Boolean>
    ) {
        runTimeoutTest {
            assertTrue(expectedEventsChecks.size == expectedStatesChecks.size)
            hub.observe().combine(
                state.observeState(),
                transform = { event, state ->
                    event to state
                }
            ).test {
                startEvent?.let { hub.emit(startEvent) }
                (expectedEventsChecks.indices).forEach { index ->
                    val item = awaitItem()
                    val (event, state) = item
                    println("current event: $event\ncurrent state: $state\n")
                    assertTrue(expectedEventsChecks[index](event))
                    assertTrue(expectedStatesChecks[index](state))
                }
                cancelAndIgnoreRemainingEvents()
            }
        }
    }*/

    protected fun interface ReducerProducer<S : Any, E : Event, R : Reducer<E, S>> {
        fun produce(): R
    }

    protected fun interface MiddlewareProducer<E : Event, MW : MapperFlowMiddleware<E>> {
        fun produce(): MW
    }

    protected fun interface ViewModelProducer<SH : Any, E : Event, VM : MviErrorHandlerViewModel<SH, E>> {
        fun produce(): VM
    }
}