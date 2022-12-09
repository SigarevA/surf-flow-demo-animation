/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.domain.entity.products

import androidx.compose.runtime.Immutable

@Immutable
data class DetailedProductEntity(
    val id: String = "",
    val name: String = "",
    val reviewsCount: Int = 0
)