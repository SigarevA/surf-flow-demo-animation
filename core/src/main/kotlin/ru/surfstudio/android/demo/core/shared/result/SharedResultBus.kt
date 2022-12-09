/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.core.shared.result

import kotlinx.coroutines.flow.Flow
import ru.surfstudio.android.demo.core.shared.result.provider.SharedResultIdProvider

/** Сущность-шина, по которой передаются общие результаты ([SharedResult]). */
interface SharedResultBus {

    /** Выпустить на шину общий результат. */
    suspend fun emit(sharedResult: SharedResult<*>)

    /** Наблюдать за результатом работы на шине по идентификатору. */
    fun <T : Any> observe(sharedResultId: String): Flow<T>

    /** Наблюдать за результатом работы на шине по идентификатору. */
    fun <T : Any> observe(sharedResultIdProvider: SharedResultIdProvider): Flow<T> {
        return observe(sharedResultIdProvider.sharedResultId)
    }
}
