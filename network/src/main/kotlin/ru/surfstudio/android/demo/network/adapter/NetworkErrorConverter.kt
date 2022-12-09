/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.adapter

import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import retrofit2.HttpException
import ru.surfstudio.android.demo.core.network.*
import ru.surfstudio.android.demo.network.generated.entry.common.ApiErrorType
import ru.surfstudio.android.demo.network.generated.entry.common.ServerErrorResponseEntry
import ru.surfstudio.android.logger.Logger
import java.net.HttpURLConnection

/** Converter from [HttpException] to [HttpProtocolException] */
internal object NetworkErrorConverter {

    fun convert(e: HttpException, json: Json): HttpProtocolException {
        val url = e.response()?.raw()?.request?.url?.toString().orEmpty()
        return when (e.code()) {
            // 500
            // based on specification: which http code is responsible for a custom error response
            HttpURLConnection.HTTP_INTERNAL_ERROR -> getApiException(e, url, json).also(Logger::e)
            // 401
            HttpURLConnection.HTTP_UNAUTHORIZED -> NonAuthorizedException(e, url)
            else -> OtherHttpException(e, e.code(), url).also(Logger::e)
        }
    }

    private fun getApiException(e: HttpException, url: String, json: Json): HttpProtocolException {
        val body = e.response()?.errorBody()?.string().orEmpty()
        val serverError: ServerErrorResponseEntry
        try {
            serverError = json.decodeFromString(body)
        } catch (error: Exception) {
            return ServerErrorParsingException(e, url, "Error while parsing server error")
        }
        val message = serverError.error_message
        return when (ApiErrorType.getByCode(serverError.code)) {
            ApiErrorType.LOGIN_INFO_INVALID -> LoginInfoInvalidException(e, url, message)
            ApiErrorType.UNKNOWN -> {
                Logger.e("Invalid server error code ${serverError.code}")
                UnknownErrorCodeException(e, url, message)
            }
        }
    }
}