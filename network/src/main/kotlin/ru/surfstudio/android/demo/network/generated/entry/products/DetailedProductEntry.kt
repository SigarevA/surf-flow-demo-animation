/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.generated.entry.products

import kotlinx.serialization.Serializable
import ru.surfstudio.android.demo.domain.entity.products.DetailedProductEntity
import ru.surfstudio.android.demo.network.Transformable

@Serializable
data class DetailedProductEntry(
    val id: String,
    val name: String,
    val reviews_count: Int
) : Transformable<DetailedProductEntity> {

    override fun transform(): DetailedProductEntity {
        return DetailedProductEntity(
            id = id,
            name = name,
            reviewsCount = reviews_count
        )
    }
}