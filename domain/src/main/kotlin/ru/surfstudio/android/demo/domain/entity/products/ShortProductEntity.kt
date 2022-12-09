/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.domain.entity.products

import androidx.compose.runtime.Immutable

@Immutable
data class ShortProductEntity(
    val id: String = "",
    val name: String = "",
)