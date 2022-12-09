/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.surfstudio.android.demo.network.room.models.TokenInfoModel

/** Dao for [TokenInfoModel] */
@Dao
interface TokenInfoModelDao {
    @Query("SELECT * FROM TokenInfoModel LIMIT 1")
    fun getModelFlow(): Flow<TokenInfoModel?>

    @Query("SELECT * FROM TokenInfoModel LIMIT 1")
    suspend fun getModel(): TokenInfoModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTokens(model: TokenInfoModel)

    @Update
    suspend fun updateTokens(model: TokenInfoModel)

    @Query("DELETE FROM TokenInfoModel")
    suspend fun clear()
}