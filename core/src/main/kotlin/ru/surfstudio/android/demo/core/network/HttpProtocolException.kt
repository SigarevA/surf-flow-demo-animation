/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.core.network

import retrofit2.HttpException

private fun prepareMessage(httpMessage: String, code: Int, url: String): String {
    return " httpCode=" + code + "\n" +
            ", httpMessage='" + httpMessage + "'" +
            ", url='" + url + "'"
}

/** Base class for each server error */
abstract class NetworkException : RuntimeException {
    constructor(cause: Throwable) : super(cause)
    constructor(message: String, cause: Throwable) : super(message, cause)
}

/** Exception wrapper for no internet error */
data class NoInternetException(override val cause: Throwable) : NetworkException(cause)

/** Base exception wrapper for each non-200 response code */
sealed class HttpProtocolException(
    cause: HttpException,
    url: String,
    val httpMessage: String = cause.message(),
    val httpCode: Int = cause.code()
) : NetworkException(prepareMessage(httpMessage, httpCode, url), cause)

/** 401 HTTP-code */
class NonAuthorizedException(
    cause: HttpException,
    url: String
) : HttpProtocolException(cause, url)

/** Login or password are incorrect (102) */
class LoginInfoInvalidException(
    cause: HttpException,
    url: String,
    message: String
) : HttpProtocolException(cause, url, message)

/** The server error code is not defined in specification */
class UnknownErrorCodeException(
    cause: HttpException,
    url: String,
    message: String
) : HttpProtocolException(cause, url, message)

/** Error while parsing server error response */
class ServerErrorParsingException(
    cause: HttpException,
    url: String,
    message: String
) : HttpProtocolException(cause, url, message)

/** Unexpected http error code */
class OtherHttpException(
    cause: HttpException,
    httpCode: Int,
    url: String
) : HttpProtocolException(cause, url, "Unknown http error code", httpCode)