/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.generated.entry.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthByPasswordRequestEntry(
    val type: String,
    val login: String,
    val password: String
)