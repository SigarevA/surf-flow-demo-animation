/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.core.mvi

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.surfstudio.android.logger.Logger

/**
 * App util for making a [Flow] from a suspend api fun
 * for usage in middleware for further mapping
 */
fun <D : Any> mviFlow(
    dispatcher: CoroutineDispatcher,
    value: suspend () -> D
): Flow<D> {
    return flow {
        delay(2000)
        this.emit(value())
    }
        .flowOn(dispatcher)
        .catch {
            Logger.d("mviFlow error", it)
            throw it // propagate exception in order to handle it further
        }
}