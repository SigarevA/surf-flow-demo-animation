/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.generated.entry.common

import kotlinx.serialization.Serializable
import ru.surfstudio.android.demo.domain.entity.common.MetadataEntity
import ru.surfstudio.android.demo.network.Transformable

@Serializable
data class MetadataEntry(
    val offset: Int,
    val limit: Int,
    val count: Int
) : Transformable<MetadataEntity> {

    override fun transform() = MetadataEntity(
        offset = offset,
        limit = limit,
        count = count
    )
}