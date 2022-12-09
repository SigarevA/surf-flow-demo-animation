/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.generated.entry.common

/** Specific server error types inside of [ServerErrorResponseEntry] */
internal enum class ApiErrorType(val code: Int) {
    LOGIN_INFO_INVALID(102),
    UNKNOWN(-1);

    companion object {

        fun getByCode(code: Int?): ApiErrorType =
            values().firstOrNull { error -> error.code == code } ?: UNKNOWN
    }
}