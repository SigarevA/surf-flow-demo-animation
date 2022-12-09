/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.debug

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jakewharton.processphoenix.ProcessPhoenix
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.surfstudio.android.demo.debug.interactor.DebugSettingsInteractor
import ru.surfstudio.android.demo.domain.ServerType
import javax.inject.Inject

@HiltViewModel
internal class DebugViewModel @Inject constructor(
    private val debugSettingsInteractor: DebugSettingsInteractor
) : ViewModel() {

    private val _serverType: MutableStateFlow<ServerType> =
        MutableStateFlow(debugSettingsInteractor.getServerTypeBlocking())

    val serverType: StateFlow<ServerType> get() = _serverType.asStateFlow()

    /**
     * Update app server type
     */
    fun setServerType(serverType: ServerType, context: Context) {
        if (_serverType.value != serverType) {
            _serverType.value = serverType
            viewModelScope.launch {
                debugSettingsInteractor.setServerType(serverType)
                ProcessPhoenix.triggerRebirth(
                    context,
                    context.packageManager.getLaunchIntentForPackage(context.packageName)
                )
            }
        }
    }
}