/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.domain.entity.common

import androidx.compose.runtime.Immutable

/**
 * @param count total item count
 * @param limit page size
 * @param offset current page offset
 */
@Immutable
data class MetadataEntity(
    val offset: Int,
    val limit: Int,
    val count: Int
)