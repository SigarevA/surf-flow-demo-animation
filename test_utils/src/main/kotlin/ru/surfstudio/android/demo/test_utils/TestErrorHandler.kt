/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.test_utils

import ru.surfstudio.mvi.mappers.handler.ErrorHandler

class TestErrorHandler : ErrorHandler {

    var errorHandled: Boolean = false
        private set

    override fun handleError(e: Throwable) {
        errorHandled = true
    }
}