/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.test.network

import app.cash.turbine.test
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertTrue
import ru.surfstudio.android.demo.core.TokenInfoProvider
import ru.surfstudio.android.demo.domain.LoginState
import ru.surfstudio.android.demo.domain.entity.auth.AuthDataEntity
import ru.surfstudio.android.demo.domain.entity.auth.TokenInfoEntity
import ru.surfstudio.android.demo.test_utils.BaseUnitTest

class SessionChangedInteractorTest : BaseUnitTest() {

    private val testRefreshToken = "testRefreshToken"
    private val testAccessToken = "testAccessToken"

    @Test
    fun `test onLogin`() = runTimeoutTest {
        sessionChangedInteractor.onLogin(
            AuthDataEntity(
                TokenInfoEntity(
                    accessToken = testAccessToken,
                    refreshToken = testRefreshToken
                )
            )
        )
        assertTrue(TokenInfoProvider.accessToken == testAccessToken)
        assertTrue(TokenInfoProvider.refreshToken == testRefreshToken)
        sessionChangedInteractor.loginStateFlow.test {
            assertTrue(awaitItem() == LoginState.LOGGED_IN)
        }
    }

    @Test
    fun `test onRefreshSession`() = runTimeoutTest {
        // previous state
        TokenInfoProvider.accessToken = "oldAccessToken"
        TokenInfoProvider.refreshToken = "oldRefreshToken"

        sessionChangedInteractor.onRefreshSession(
            TokenInfoEntity(
                accessToken = testAccessToken,
                refreshToken = testRefreshToken
            )
        )
        assertTrue(TokenInfoProvider.accessToken == testAccessToken)
        assertTrue(TokenInfoProvider.refreshToken == testRefreshToken)
        sessionChangedInteractor.loginStateFlow.test {
            assertTrue(awaitItem() == LoginState.LOGGED_IN)
        }
    }

    @Test
    fun `test onLogout`() = runTimeoutTest {
        sessionChangedInteractor.onLogout()
        sessionChangedInteractor.loginStateFlow.test {
            assertTrue(awaitItem() == LoginState.NOT_AUTHORIZED)
        }
    }
}