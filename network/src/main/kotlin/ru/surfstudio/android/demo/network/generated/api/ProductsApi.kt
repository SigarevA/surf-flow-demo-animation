/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.generated.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.surfstudio.android.demo.network.generated.entry.products.DetailedProductEntry
import ru.surfstudio.android.demo.network.generated.entry.products.ProductsListEntry
import ru.surfstudio.android.demo.network.generated.urls.ProductsUrls

interface ProductsApi {

    @GET(ProductsUrls.GET_PRODUCTS)
    suspend fun getProducts(
        @Query("category_id") categoryId: String,
        @Query("sort_type") sortType: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): ProductsListEntry

    @GET(ProductsUrls.GET_PRODUCT_DETAILS)
    suspend fun getProductDetails(
        @Query("product_id") productId: String
    ): DetailedProductEntry
}