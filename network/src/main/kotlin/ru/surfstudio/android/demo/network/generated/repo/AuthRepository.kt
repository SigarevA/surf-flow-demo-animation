/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.generated.repo

import ru.surfstudio.android.demo.domain.entity.auth.AuthDataEntity
import ru.surfstudio.android.demo.domain.entity.auth.TokenInfoEntity
import ru.surfstudio.android.demo.network.generated.api.AuthApi
import ru.surfstudio.android.demo.network.generated.entry.auth.AuthByPasswordRequestEntry
import ru.surfstudio.android.demo.network.generated.entry.auth.RefreshTokenRequestEntry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val api: AuthApi) {

    suspend fun authByPassword(
        type: String,
        login: String,
        password: String
    ): AuthDataEntity =
        api.authByPassword(
            AuthByPasswordRequestEntry(
                type = type,
                login = login,
                password = password
            )
        ).transform()

    suspend fun refreshToken(token: String): TokenInfoEntity =
        api.refreshToken(RefreshTokenRequestEntry(token)).transform()

    suspend fun logout(): Unit =
        api.logout().body() ?: Unit
}