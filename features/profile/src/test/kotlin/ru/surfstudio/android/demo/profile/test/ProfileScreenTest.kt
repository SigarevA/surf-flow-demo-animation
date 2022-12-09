/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.profile.test

import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Test
import ru.surfstudio.android.demo.core.mvi.mapper.LoadStateType
import ru.surfstudio.android.demo.domain.LoginState
import ru.surfstudio.android.demo.domain.entity.auth.AuthDataEntity
import ru.surfstudio.android.demo.domain.entity.auth.TokenInfoEntity
import ru.surfstudio.android.demo.network.generated.interactor.AuthInteractor
import ru.surfstudio.android.demo.profile.ProfileEvent
import ru.surfstudio.android.demo.profile.ProfileMiddleware
import ru.surfstudio.android.demo.profile.ProfileReducer
import ru.surfstudio.android.demo.profile.ProfileViewModel
import ru.surfstudio.android.demo.test_utils.BaseMviScreenTest
import ru.surfstudio.android.demo.test_utils.BaseMviScreenTest.*
import ru.surfstudio.android.demo.test_utils.TestErrorHandler

class ProfileScreenTest : BaseMviScreenTest() {

    private val testAuthData = AuthDataEntity(
        TokenInfoEntity(
            refreshToken = "refreshToken",
            accessToken = "accessToken",
        )
    )
    private val errorHandler = TestErrorHandler()
    private val authInteractor = mockk<AuthInteractor> {
        coEvery { authByPassword(any(), any(), any()) } returns testAuthData
        coEvery { logout() } returns Unit
    }
    private val reducerProducer = ReducerProducer { ProfileReducer(errorHandler) }
    private val middlewareProducer = MiddlewareProducer {
        ProfileMiddleware(testDispatcher, authInteractor, sessionChangedInteractor)
    }
    private val viewModelProducer = ViewModelProducer {
        ProfileViewModel(reducerProducer.produce(), middlewareProducer.produce())
    }

    @Test
    fun `test login state flow`() {
        viewModelProducer.produce().mviScreenTest(
            startAction = {
                sessionChangedInteractor.onLogin(testAuthData)
            },
            expectedData = listOf(
                { it.state!!.loadState == LoadStateType.None },
                { (it.event as ProfileEvent.LoginStateChangedEvent).loginState == LoginState.LOGGED_IN },
                { it.state!!.isAuthorized }
            )
        )
    }

    @Test
    fun `test login succeed`() {
        val startEvent = ProfileEvent.LoginClicked
        viewModelProducer.produce().mviScreenTest(
            startEvent = startEvent,
            expectedData = listOf(
                { it.state!!.loadState == LoadStateType.None },
                { it.event == startEvent },
                { (it.event as ProfileEvent.DataLoad.LoginRequest).isLoading },
                { it.state!!.loadState == LoadStateType.TransparentLoading },
                { (it.event as ProfileEvent.DataLoad.LoginRequest).request.getData() == testAuthData },
                // isAuthorized is checked for login state flow separately
                { it.state!!.loadState == LoadStateType.None }
            )
        )
    }

    @Test
    fun `test logout succeed`() {
        val startEvent = ProfileEvent.LogoutClicked
        viewModelProducer.produce().mviScreenTest(
            startEvent = startEvent,
            expectedData = listOf(
                { it.state!!.loadState == LoadStateType.None },
                { it.event == startEvent },
                { (it.event as ProfileEvent.DataLoad.LogoutRequest).isLoading },
                { it.state!!.loadState == LoadStateType.TransparentLoading },
                { (it.event as ProfileEvent.DataLoad.LogoutRequest).isSuccess },
                // isAuthorized is checked for login state flow separately
                { it.state!!.loadState == LoadStateType.None }
            )
        )
    }
}