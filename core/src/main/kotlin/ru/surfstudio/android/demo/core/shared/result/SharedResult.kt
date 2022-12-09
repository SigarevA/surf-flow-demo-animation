/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.core.shared.result

import ru.surfstudio.android.demo.core.shared.result.provider.SharedResultDataProvider
import ru.surfstudio.android.demo.core.shared.result.provider.SharedResultIdProvider

/**
 * Сущность, которая является результатом работы чего-либо.
 * Предназначена для отправки по [SharedResultBus].
 * */
interface SharedResult<T : Any> : SharedResultDataProvider<T>, SharedResultIdProvider