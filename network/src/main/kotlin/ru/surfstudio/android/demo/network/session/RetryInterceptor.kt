/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.session

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import ru.surfstudio.android.demo.core.Constants.okhttpTag
import ru.surfstudio.android.demo.core.TokenInfoProvider
import ru.surfstudio.android.demo.network.BaseUrl
import ru.surfstudio.android.demo.network.generated.entry.auth.RefreshTokenRequestEntry
import ru.surfstudio.android.demo.network.generated.entry.auth.TokenResponseEntry
import ru.surfstudio.android.demo.network.generated.urls.AuthUrls
import ru.surfstudio.android.logger.Logger
import java.io.IOException
import java.lang.Exception
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton

/** Refresh token and retry previous request for 401 error */
@Singleton
internal class RetryInterceptor @Inject constructor(
    @BaseUrl baseUrl: String,
    private val json: Json,
    private val sessionChangedInteractor: SessionChangedInteractor
) : Interceptor {

    private val refreshTokenUrl = baseUrl + AuthUrls.REFRESH_TOKEN

    private val refreshToken: String?
        get() = TokenInfoProvider.refreshToken

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code != HttpURLConnection.HTTP_UNAUTHORIZED) {
            return response
        }
        if (refreshToken.isNullOrEmpty()) {
            Logger.w(okhttpTag, "No token is present for auto-refresh after 401 error")
            return response
        }
        response.close()
        return tryToRefresh(chain, refreshToken!!)
    }

    @Synchronized
    private fun tryToRefresh(chain: Interceptor.Chain, previousToken: String): Response {
        // refreshToken could be updated from another thread
        if (refreshToken != previousToken) {
            return chain.proceed(chain.request())
        }
        with(requestNewAccessToken(chain)) {
            if (code != HttpURLConnection.HTTP_OK) return this
            val tokenResponse: TokenResponseEntry
            try {
                tokenResponse = json.decodeFromString(body?.string().orEmpty())
            } catch (e: Exception) {
                throw IOException("Error parsing token response during auto-refresh")
            }
            // lightweight suspend fun: emits value and launches further coroutines
            runBlocking {
                sessionChangedInteractor.onRefreshSession(tokenInfo = tokenResponse.transform())
            }
            return retryPreviousRequest(chain)
        }
    }

    @Throws(IOException::class)
    private fun requestNewAccessToken(chain: Interceptor.Chain): Response {
        val chainRequest = chain.request()
        val request = chainRequest
            .newBuilder()
            .url(refreshTokenUrl)
            .addRefreshTokenBody()
            .build()
        return chain.proceed(request)
    }

    @Throws(IOException::class)
    private fun retryPreviousRequest(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .build()
        return chain.proceed(request)
    }

    private fun Request.Builder.addRefreshTokenBody(): Request.Builder =
        apply {
            refreshToken?.let {
                post(
                    json.encodeToString(RefreshTokenRequestEntry(token = it))
                        .toRequestBody()
                )
            }
        }
}