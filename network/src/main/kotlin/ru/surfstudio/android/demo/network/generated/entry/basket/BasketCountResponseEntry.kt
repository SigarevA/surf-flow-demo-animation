/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.generated.entry.basket

import kotlinx.serialization.Serializable
import ru.surfstudio.android.demo.network.Transformable

@Serializable
data class BasketCountResponseEntry(
    val count: Int
) : Transformable<Int> {

    override fun transform(): Int = count
}
