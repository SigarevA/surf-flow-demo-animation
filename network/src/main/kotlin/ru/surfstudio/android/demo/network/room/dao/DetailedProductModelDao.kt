/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.surfstudio.android.demo.network.room.models.DetailedProductModel

/** Dao for [DetailedProductModel] */
@Dao
interface DetailedProductModelDao {

    @Query("SELECT * FROM DetailedProductModel WHERE id = :id LIMIT 1")
    fun getModelFlow(id: String): Flow<DetailedProductModel?>

    @Query("SELECT * FROM DetailedProductModel WHERE id = :id LIMIT 1")
    suspend fun getModel(id: String): DetailedProductModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: DetailedProductModel)

    @Query("DELETE FROM DetailedProductModel")
    suspend fun clear()
}