/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.session

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ru.surfstudio.android.demo.core.TokenInfoProvider
import javax.inject.Inject
import javax.inject.Singleton

/** Adds required headers for each request */
@Singleton
internal class ServiceInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request().addHeaders())
    }

    private fun Request.addHeaders(): Request {
        val headersBuilder = headers.newBuilder()
            .apply {
                addIfNotEmpty(AppHeaders.AUTH_HEADER_NAME, TokenInfoProvider.accessToken)
            }
        return newBuilder()
            .headers(headersBuilder.build())
            .build()
    }

    private fun Headers.Builder.addIfNotEmpty(
        name: String,
        value: String?
    ): Headers.Builder {
        return apply {
            if (!value.isNullOrEmpty()) {
                add(name, value)
            }
        }
    }
}