/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.profile

import androidx.compose.runtime.Stable
import dagger.hilt.android.scopes.ViewModelScoped
import ru.surfstudio.android.demo.core.mvi.mapper.LoadStateType
import ru.surfstudio.android.demo.core.mvi.mapper.RequestMappers
import ru.surfstudio.android.demo.domain.LoginState
import ru.surfstudio.mvi.mappers.RequestEvent
import ru.surfstudio.mvi.mappers.RequestMapper
import ru.surfstudio.mvi.mappers.handler.ErrorHandler
import ru.surfstudio.mvi.mappers.handler.ErrorHandlerReducer
import javax.inject.Inject

@Stable
data class ProfileState(
    val isAuthorized: Boolean = false,
    val loadState: LoadStateType = LoadStateType.None
) {
    val uiState: String = "Authorized".takeIf { isAuthorized } ?: "Not authorized"
}

@ViewModelScoped
class ProfileReducer @Inject constructor(
    override val errorHandler: ErrorHandler
) : ErrorHandlerReducer<ProfileEvent, ProfileState> {

    override fun reduce(state: ProfileState, event: ProfileEvent): ProfileState {
        return when (event) {
            is ProfileEvent.LoginStateChangedEvent -> state.copy(
                isAuthorized = event.loginState == LoginState.LOGGED_IN
            )
            is ProfileEvent.DataLoad.LoginRequest -> state.updateRequestUi(event)
            is ProfileEvent.DataLoad.LogoutRequest -> state.updateRequestUi(event)
            else -> state
        }
    }

    private fun <T : Any> ProfileState.updateRequestUi(event: RequestEvent<T>): ProfileState {
        return copy(
            loadState = RequestMapper
                .builder(event.request)
                .mapData(RequestMappers.data.default())
                .mapLoading(RequestMappers.loading.transparentOrNone())
                .handleError(RequestMappers.error.forced(errorHandler))
                .build()
                .load as? LoadStateType ?: loadState
        )
    }
}