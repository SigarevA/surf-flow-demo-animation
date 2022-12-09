/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.generated.interactor

import androidx.paging.PagingSource
import androidx.room.withTransaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import ru.surfstudio.android.demo.core.Constants
import ru.surfstudio.android.demo.domain.entity.products.DetailedProductEntity
import ru.surfstudio.android.demo.domain.entity.products.ProductsListEntity
import ru.surfstudio.android.demo.network.CacheInteractor
import ru.surfstudio.android.demo.network.generated.repo.ProductsRepository
import ru.surfstudio.android.demo.network.room.dao.DetailedProductModelDao
import ru.surfstudio.android.demo.network.room.dao.ShortProductModelDao
import ru.surfstudio.android.demo.network.room.database.AppDatabase
import ru.surfstudio.android.demo.network.room.models.ShortProductModel
import ru.surfstudio.android.demo.network.room.models.toModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductsInteractor @Inject constructor(
    private val db: AppDatabase,
    private val repository: ProductsRepository
) : CacheInteractor {

    private val shortProductDao: ShortProductModelDao = db.shortProductModelDao()
    private val detailedProductDao: DetailedProductModelDao = db.detailedProductModelDao()

    suspend fun updateProductsCache(
        delete: Boolean,
        categoryId: String,
        products: List<ShortProductModel>
    ) {
        db.withTransaction {
            if (delete) {
                shortProductDao.deleteBy(categoryId)
            }
            shortProductDao.insertAll(products)
        }
    }

    fun getProductsCache(categoryId: String): PagingSource<Int, ShortProductModel> =
        shortProductDao.pagingSource(categoryId)

    suspend fun getProducts(
        categoryId: String,
        offset: Int,
        sortType: String = "popular", // const for example
        limit: Int = Constants.pageSize
    ): ProductsListEntity =
        repository.getProducts(
            categoryId = categoryId,
            sortType = sortType,
            offset = offset,
            limit = limit
        )

    @ExperimentalCoroutinesApi
    fun getProductDetails(
        productId: String,
        fromCache: Boolean = true,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<DetailedProductEntity> =
        getCachedData(
            fromCache = fromCache,
            dispatcher = dispatcher,
            cacheRequest = { detailedProductDao.getModel(productId) },
            networkRequest = { repository.getProductDetails(productId) },
            updateCacheRequest = { detailedProductDao.insert(it.toModel()) }
        )
}