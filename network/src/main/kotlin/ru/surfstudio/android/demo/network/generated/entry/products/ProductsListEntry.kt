/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.generated.entry.products

import kotlinx.serialization.Serializable
import ru.surfstudio.android.demo.domain.entity.products.ProductsListEntity
import ru.surfstudio.android.demo.network.Transformable
import ru.surfstudio.android.demo.network.generated.entry.common.MetadataEntry
import ru.surfstudio.android.demo.network.transformCollection

@Serializable
data class ProductsListEntry(
    val products: List<ShortProductEntry>,
    val metadata: MetadataEntry
) : Transformable<ProductsListEntity> {

    override fun transform() = ProductsListEntity(
        products = products.transformCollection(),
        metadata = metadata.transform()
    )
}