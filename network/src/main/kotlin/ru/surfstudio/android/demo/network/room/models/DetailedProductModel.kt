/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.surfstudio.android.demo.domain.entity.products.DetailedProductEntity
import ru.surfstudio.android.demo.network.Transformable
import ru.surfstudio.android.demo.network.room.models.base.IModel

@Entity
data class DetailedProductModel(
    @PrimaryKey override val id: String,
    val name: String,
    val reviewsCount: Int
) : IModel, Transformable<DetailedProductEntity> {

    override fun transform(): DetailedProductEntity {
        return DetailedProductEntity(
            id = id,
            name = name,
            reviewsCount = reviewsCount
        )
    }
}

internal fun DetailedProductEntity.toModel() = DetailedProductModel(
    id = id,
    name = name,
    reviewsCount = reviewsCount
)