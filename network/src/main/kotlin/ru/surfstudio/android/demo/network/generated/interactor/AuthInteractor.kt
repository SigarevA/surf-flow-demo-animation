/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.generated.interactor

import ru.surfstudio.android.demo.domain.entity.auth.AuthDataEntity
import ru.surfstudio.android.demo.domain.entity.auth.TokenInfoEntity
import ru.surfstudio.android.demo.network.generated.repo.AuthRepository
import ru.surfstudio.android.demo.network.session.SessionChangedInteractor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInteractor @Inject constructor(
    private val repository: AuthRepository,
    private val sessionChangedInteractor: SessionChangedInteractor
) {

    suspend fun authByPassword(
        type: String,
        login: String,
        password: String
    ): AuthDataEntity =
        repository.authByPassword(type = type, login = login, password = password)
            .also { sessionChangedInteractor.onLogin(it) }

    suspend fun refreshToken(token: String): TokenInfoEntity =
        repository.refreshToken(token)
            .also { sessionChangedInteractor.onRefreshSession(it) }

    suspend fun logout(): Unit =
        repository.logout()
            .also { sessionChangedInteractor.onLogout() }
}