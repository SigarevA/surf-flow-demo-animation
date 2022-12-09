/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.generated.entry.products

import kotlinx.serialization.Serializable
import ru.surfstudio.android.demo.domain.entity.products.ShortProductEntity
import ru.surfstudio.android.demo.network.Transformable

@Serializable
data class ShortProductEntry(
    val id: String,
    val name: String,
) : Transformable<ShortProductEntity> {

    override fun transform() = ShortProductEntity(
        id = id,
        name = name
    )
}