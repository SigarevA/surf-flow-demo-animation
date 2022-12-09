/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.core.shared.result.impl

import kotlinx.coroutines.flow.*
import ru.surfstudio.android.demo.core.shared.result.SharedResult
import ru.surfstudio.android.demo.core.shared.result.SharedResultBus

@Suppress("UNCHECKED_CAST")
class SharedResultBusImpl : SharedResultBus {

    private val mutableSharedFlow = MutableSharedFlow<SharedResult<*>>()

    override suspend fun emit(sharedResult: SharedResult<*>) {
        mutableSharedFlow.emit(sharedResult)
    }

    override fun <T : Any> observe(sharedResultId: String): Flow<T> {
        return mutableSharedFlow
            .asSharedFlow()
            .filter { it.sharedResultId == sharedResultId }
            .map { it.sharedResultData as T }
    }
}