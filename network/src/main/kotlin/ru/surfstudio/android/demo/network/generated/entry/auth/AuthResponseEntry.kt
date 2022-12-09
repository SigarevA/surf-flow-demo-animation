/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.generated.entry.auth

import kotlinx.serialization.Serializable
import ru.surfstudio.android.demo.domain.entity.auth.AuthDataEntity
import ru.surfstudio.android.demo.network.Transformable

@Serializable
data class AuthResponseEntry(
    val tokens: TokenResponseEntry
): Transformable<AuthDataEntity> {

    override fun transform() = AuthDataEntity(
        tokenInfo = tokens.transform()
    )
}