/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.surfstudio.android.demo.domain.entity.auth.TokenInfoEntity
import ru.surfstudio.android.demo.network.Transformable
import ru.surfstudio.android.demo.network.room.models.base.IModel
import ru.surfstudio.android.demo.core.util.SecurityUtil

// alias can be static or dynamic (for example, pin code entered by user)
private const val ACCESS_TOKEN_ALIAS = "ACCESS_TOKEN_ALIAS"
private const val REFRESH_TOKEN_ALIAS = "REFRESH_TOKEN_ALIAS"

/**
 * A single instance in DB is intended (insert onLogin, delete onLogout, update onRefresh)
 */
@Entity
data class TokenInfoModel(
    val accessToken: String,
    val refreshToken: String
) : IModel, Transformable<TokenInfoEntity> {

    @PrimaryKey
    override var id: String = "TokenInfoModel"

    override fun transform() = TokenInfoEntity(
        accessToken = accessToken,
        refreshToken = refreshToken
    )

    /** Returns encrypted [TokenInfoModel] */
    fun encrypt() = TokenInfoModel(
        accessToken = SecurityUtil.encrypt(ACCESS_TOKEN_ALIAS, accessToken),
        refreshToken = SecurityUtil.encrypt(REFRESH_TOKEN_ALIAS, refreshToken)
    )

    /** Returns decrypted [TokenInfoModel] */
    fun decrypt() = TokenInfoModel(
        accessToken = SecurityUtil.decrypt(ACCESS_TOKEN_ALIAS, accessToken).orEmpty(),
        refreshToken = SecurityUtil.decrypt(REFRESH_TOKEN_ALIAS, refreshToken).orEmpty()
    )
}

internal fun TokenInfoEntity.toModel() = TokenInfoModel(
    accessToken = accessToken,
    refreshToken = refreshToken
)