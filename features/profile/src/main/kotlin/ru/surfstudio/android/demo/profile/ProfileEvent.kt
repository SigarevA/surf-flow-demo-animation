/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.profile

import ru.surfstudio.android.demo.domain.LoginState
import ru.surfstudio.android.demo.domain.entity.auth.AuthDataEntity
import ru.surfstudio.mvi.core.event.Event
import ru.surfstudio.mvi.mappers.Request
import ru.surfstudio.mvi.mappers.RequestEvent

sealed class ProfileEvent : Event {
    data class LoginStateChangedEvent(val loginState: LoginState) : ProfileEvent()
    object LoginClicked : ProfileEvent()
    object LogoutClicked : ProfileEvent()

    sealed class DataLoad<T : Any> : ProfileEvent(), RequestEvent<T> {
        data class LoginRequest(
            override val request: Request<AuthDataEntity>
        ) : DataLoad<AuthDataEntity>()

        data class LogoutRequest(
            override val request: Request<Unit>
        ) : DataLoad<Unit>()
    }
}