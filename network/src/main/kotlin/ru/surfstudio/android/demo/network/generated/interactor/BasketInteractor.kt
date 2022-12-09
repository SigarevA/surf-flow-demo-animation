/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.generated.interactor

import ru.surfstudio.android.demo.network.generated.repo.BasketRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BasketInteractor @Inject constructor(
    private val repository: BasketRepository
) {

    suspend fun getBasketCount(): Int =
        repository.getBasketCount()
}