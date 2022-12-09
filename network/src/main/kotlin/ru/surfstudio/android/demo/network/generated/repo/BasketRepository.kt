/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.generated.repo

import ru.surfstudio.android.demo.network.generated.api.BasketApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BasketRepository @Inject constructor(private val api: BasketApi) {

    suspend fun getBasketCount(): Int =
        api.getBasketCount().transform()
}