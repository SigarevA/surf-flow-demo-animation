/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.test_utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.surfstudio.android.demo.core.shared.result.SharedResult
import ru.surfstudio.android.demo.core.shared.result.SharedResultBus

class TestSharedResultBus : SharedResultBus {

    var resultId: String? = null
        private set

    override suspend fun emit(sharedResult: SharedResult<*>) {
        resultId = sharedResult.sharedResultId
    }

    override fun <T : Any> observe(sharedResultId: String): Flow<T> {
        return emptyFlow()
    }
}