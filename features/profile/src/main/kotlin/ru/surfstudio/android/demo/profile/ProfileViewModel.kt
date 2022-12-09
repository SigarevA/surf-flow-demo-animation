/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.profile

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.surfstudio.mvi.flow.FlowEventHub
import ru.surfstudio.mvi.flow.FlowState
import ru.surfstudio.mvi.mappers.handler.MviErrorHandlerViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    override val reducer: ProfileReducer,
    override val middleware: ProfileMiddleware
) : MviErrorHandlerViewModel<ProfileState, ProfileEvent>() {

    override val state: FlowState<ProfileState> = FlowState(ProfileState())
    override val hub: FlowEventHub<ProfileEvent> = FlowEventHub()

    init {
        bindFlow()
    }
}