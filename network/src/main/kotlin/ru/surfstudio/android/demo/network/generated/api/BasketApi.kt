/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.generated.api

import retrofit2.http.GET
import ru.surfstudio.android.demo.network.generated.entry.basket.BasketCountResponseEntry
import ru.surfstudio.android.demo.network.generated.urls.BasketUrls

interface BasketApi {

    @GET(BasketUrls.GET_COUNT)
    suspend fun getBasketCount(): BasketCountResponseEntry
}