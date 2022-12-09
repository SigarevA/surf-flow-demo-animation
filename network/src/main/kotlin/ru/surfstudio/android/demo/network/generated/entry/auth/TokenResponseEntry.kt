/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.generated.entry.auth

import kotlinx.serialization.Serializable
import ru.surfstudio.android.demo.domain.entity.auth.TokenInfoEntity
import ru.surfstudio.android.demo.network.Transformable

@Serializable
data class TokenResponseEntry(
    val access_token: String,
    val refresh_token: String
) : Transformable<TokenInfoEntity> {

    override fun transform() = TokenInfoEntity(
        accessToken = access_token,
        refreshToken = refresh_token
    )
}