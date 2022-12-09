/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.generated.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.surfstudio.android.demo.network.generated.entry.auth.AuthByPasswordRequestEntry
import ru.surfstudio.android.demo.network.generated.entry.auth.AuthResponseEntry
import ru.surfstudio.android.demo.network.generated.entry.auth.RefreshTokenRequestEntry
import ru.surfstudio.android.demo.network.generated.entry.auth.TokenResponseEntry
import ru.surfstudio.android.demo.network.generated.urls.AuthUrls

interface AuthApi {

    @POST(AuthUrls.AUTH_BY_PASSWORD)
    suspend fun authByPassword(@Body request: AuthByPasswordRequestEntry): AuthResponseEntry

    @POST(AuthUrls.REFRESH_TOKEN)
    suspend fun refreshToken(@Body request: RefreshTokenRequestEntry): TokenResponseEntry

    @POST(AuthUrls.LOGOUT)
    suspend fun logout(): Response<Unit> // 204 or 201 HTTP code
}