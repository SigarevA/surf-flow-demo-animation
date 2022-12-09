/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.session

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.surfstudio.android.demo.core.ScreenDispatcher
import ru.surfstudio.android.demo.core.TokenInfoProvider
import ru.surfstudio.android.demo.core.logger.RemoteLogger
import ru.surfstudio.android.demo.domain.LoginState
import ru.surfstudio.android.demo.domain.entity.auth.AuthDataEntity
import ru.surfstudio.android.demo.domain.entity.auth.TokenInfoEntity
import ru.surfstudio.android.demo.network.room.dao.TokenInfoModelDao
import ru.surfstudio.android.demo.network.room.database.AppEncryptedDatabase
import ru.surfstudio.android.demo.network.room.models.toModel
import javax.inject.Inject
import javax.inject.Singleton

/** Инкапсулирует действия, которые необходимо выполнить при смене сессии/пользователя */
@Singleton
class SessionChangedInteractor @Inject constructor(
    private val appEncryptedDatabase: AppEncryptedDatabase,
    @ScreenDispatcher private val dispatcher: CoroutineDispatcher,
) {

    private val _loginStateFlow: MutableSharedFlow<LoginState> = MutableSharedFlow(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val loginStateFlow: Flow<LoginState> = _loginStateFlow.asSharedFlow()

    private val tokenInfoModelDao: TokenInfoModelDao
        get() = appEncryptedDatabase.tokenInfoModelDao()

    private var jobLogin: Job? = null
    private var jobRefresh: Job? = null
    private var jobLogout: Job? = null

    /** Get previously saved tokens on app start and save to RAM */
    suspend fun provideSavedTokens() {
        withContext(dispatcher) {
            tokenInfoModelDao.getModel()
                ?.also { saveTokensToRAM(it.decrypt().transform()) }
        }
    }

    suspend fun onLogin(data: AuthDataEntity) {
        RemoteLogger.setUser(id = "", username = "", email = "") //todo change for real app
        saveTokensToRAM(data.tokenInfo)
        insertTokens(data.tokenInfo)
    }

    suspend fun onRefreshSession(tokenInfo: TokenInfoEntity) {
        saveTokensToRAM(tokenInfo)
        jobRefresh?.cancel()
        updateTokens(tokenInfo)
    }

    suspend fun onLogout() {
        RemoteLogger.clearUser()
        // todo clear all storages
        _loginStateFlow.emit(LoginState.NOT_AUTHORIZED)
        clearTokens()
    }

    private suspend fun saveTokensToRAM(tokenInfo: TokenInfoEntity) {
        TokenInfoProvider.accessToken = tokenInfo.accessToken
        TokenInfoProvider.refreshToken = tokenInfo.refreshToken
        _loginStateFlow.emit(LoginState.LOGGED_IN)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun insertTokens(tokenInfo: TokenInfoEntity) {
        jobLogin?.cancel()
        CoroutineScope(dispatcher).launch {
            jobLogin = launch(dispatcher) {
                tokenInfoModelDao.insertTokens(tokenInfo.toModel().encrypt())
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun updateTokens(tokenInfo: TokenInfoEntity) {
        jobRefresh?.cancel()
        CoroutineScope(dispatcher).launch {
            jobRefresh = launch(dispatcher) {
                tokenInfoModelDao.updateTokens(tokenInfo.toModel().encrypt())
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun clearTokens() {
        jobLogout?.cancel()
        CoroutineScope(dispatcher).launch {
            jobLogout = launch(dispatcher) {
                tokenInfoModelDao.clear()
            }
        }
    }
}