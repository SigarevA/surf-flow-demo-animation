/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.domain.entity.products

import androidx.compose.runtime.Immutable
import ru.surfstudio.android.demo.domain.entity.common.MetadataEntity

@Immutable
data class ProductsListEntity(
    val products: List<ShortProductEntity>,
    val metadata: MetadataEntity
)