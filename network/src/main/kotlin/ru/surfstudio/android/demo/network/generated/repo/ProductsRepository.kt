/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.generated.repo

import ru.surfstudio.android.demo.domain.entity.products.DetailedProductEntity
import ru.surfstudio.android.demo.domain.entity.products.ProductsListEntity
import ru.surfstudio.android.demo.network.generated.api.ProductsApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductsRepository @Inject constructor(private val api: ProductsApi) {

    suspend fun getProducts(
        categoryId: String,
        sortType: String,
        offset: Int,
        limit: Int
    ): ProductsListEntity =
        api.getProducts(
            categoryId = categoryId,
            sortType = sortType,
            offset = offset,
            limit = limit
        ).transform()

    suspend fun getProductDetails(productId: String): DetailedProductEntity =
        api.getProductDetails(productId = productId).transform()
}