/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.profile

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.surfstudio.android.demo.core.ScreenDispatcher
import ru.surfstudio.android.demo.core.mvi.mviFlow
import ru.surfstudio.android.demo.network.generated.interactor.AuthInteractor
import ru.surfstudio.android.demo.network.session.SessionChangedInteractor
import ru.surfstudio.android.demo.profile.ProfileEvent.DataLoad.LoginRequest
import ru.surfstudio.android.demo.profile.ProfileEvent.DataLoad.LogoutRequest
import ru.surfstudio.mvi.mappers.MapperFlowMiddleware
import javax.inject.Inject

@ViewModelScoped
class ProfileMiddleware @Inject constructor(
    @ScreenDispatcher private val dispatcher: CoroutineDispatcher,
    private val authInteractor: AuthInteractor,
    private val sessionChangedInteractor: SessionChangedInteractor
) : MapperFlowMiddleware<ProfileEvent> {

    override fun transform(eventStream: Flow<ProfileEvent>): Flow<ProfileEvent> {
        return eventStream.transformations {
            addAll(
                collectLoginStateFlow(),
                ProfileEvent.LoginClicked::class eventToStream { login() },
                ProfileEvent.LogoutClicked::class eventToStream { logout() }
            )
        }
    }

    private fun collectLoginStateFlow(): Flow<ProfileEvent> =
        sessionChangedInteractor.loginStateFlow
            .map { ProfileEvent.LoginStateChangedEvent(it) }

    private fun login(): Flow<ProfileEvent> =
        mviFlow(dispatcher) {
            authInteractor.authByPassword(
                type = "phone",
                login = "71027777777",
                password = "qwer"
            )
        }
            .asRequestEvent(::LoginRequest)

    private fun logout(): Flow<ProfileEvent> =
        mviFlow(dispatcher) { authInteractor.logout() }
            .asRequestEvent(::LogoutRequest)
}