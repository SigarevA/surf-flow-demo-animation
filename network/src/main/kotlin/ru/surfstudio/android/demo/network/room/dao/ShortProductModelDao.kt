/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.surfstudio.android.demo.network.room.models.ShortProductModel

/** Dao for [ShortProductModel] */
@Dao
interface ShortProductModelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<ShortProductModel>)

    @Query("SELECT * FROM ShortProductModel WHERE categoryId = :categoryId")
    fun pagingSource(categoryId: String): PagingSource<Int, ShortProductModel>

    @Query("DELETE FROM ShortProductModel WHERE categoryId = :categoryId")
    suspend fun deleteBy(categoryId: String)

    @Query("DELETE FROM ShortProductModel")
    suspend fun clear()
}