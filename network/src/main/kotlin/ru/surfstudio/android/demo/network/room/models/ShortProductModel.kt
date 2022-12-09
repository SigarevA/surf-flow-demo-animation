/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.surfstudio.android.demo.domain.entity.products.ShortProductEntity
import ru.surfstudio.android.demo.network.Transformable
import ru.surfstudio.android.demo.network.room.models.base.IModel

@Entity
data class ShortProductModel(
    @PrimaryKey override val id: String,
    val name: String,
    val categoryId: String
) : IModel, Transformable<ShortProductEntity> {

    override fun transform() = ShortProductEntity(
        id = id,
        name = name
    )
}

internal fun ShortProductEntity.toModel(categoryId: String) = ShortProductModel(
    id = id,
    name = name,
    categoryId = categoryId
)