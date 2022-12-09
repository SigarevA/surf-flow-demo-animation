/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.core.mvi.error

import android.content.Context
import ru.surfstudio.android.demo.core.network.HttpProtocolException
import ru.surfstudio.android.demo.core.network.NoInternetException
import ru.surfstudio.android.demo.core.network.NonAuthorizedException
import ru.surfstudio.android.demo.core.snackbar.IconMessageController
import ru.surfstudio.android.demo.resources.R
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.mvi.mappers.handler.ErrorHandler
import java.net.HttpURLConnection
import java.util.concurrent.CancellationException

/** Base app error handler */
internal class ErrorHandlerImpl(
    private val messageController: IconMessageController,
    private val context: Context
) : ErrorHandler {

    override fun handleError(e: Throwable) {
        Logger.i(e, "ErrorHandlerImpl handleError")
        when (e) {
            is HttpProtocolException -> handleHttpProtocolException(e)
            is NoInternetException -> handleNoInternetException()
            else -> handleOtherException(e)
        }
    }

    private fun handleHttpProtocolException(e: HttpProtocolException) {
        // handled in RetryInterceptor
        if (e is NonAuthorizedException) {
            return
        }

        val message = e.httpMessage.takeIf { it.isNotEmpty() }
            ?: context.getString(R.string.default_server_error_message)

        when {
            // >= 500
            e.httpCode >= HttpURLConnection.HTTP_INTERNAL_ERROR ->
                messageController.showErrorMessage(message)
            // 403
            e.httpCode == HttpURLConnection.HTTP_FORBIDDEN ->
                messageController.showErrorMessage(R.string.forbidden_error_message)
            else ->
                messageController.showErrorMessage(R.string.default_server_error_message)
        }
    }

    private fun handleNoInternetException() {
        messageController.showErrorMessage(R.string.no_internet_error_message)
    }

    private fun handleOtherException(e: Throwable) {
        if (e is CancellationException) {
            return
        }
        Logger.e("Unexpected error", e)
        messageController.showErrorMessage(R.string.default_server_error_message)
    }
}