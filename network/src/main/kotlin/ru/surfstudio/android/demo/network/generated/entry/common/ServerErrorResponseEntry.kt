/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.generated.entry.common

import kotlinx.serialization.Serializable

/**
 * Custom server error response for 500 HTTP-code.
 * Unique for each app according to specification
 */
@Serializable
internal data class ServerErrorResponseEntry(
    val code: Int,
    val error_message: String,
    val data: String?
)