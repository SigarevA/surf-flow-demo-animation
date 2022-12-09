/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.test.core

import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import ru.surfstudio.android.demo.core.network.NonAuthorizedException

fun getTestNonAuthorizedException() = NonAuthorizedException(
    cause = HttpException(
        Response.error<String>(401, "".toResponseBody())
    ),
    url = ""
)