/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.core.shared.result.impl

import ru.surfstudio.android.demo.core.shared.result.SharedResult

class SharedResultImpl<T : Any>(
    override val sharedResultData: T,
    override val sharedResultId: String
) : SharedResult<T>