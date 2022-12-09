/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.surfstudio.android.demo.network.room.dao.DetailedProductModelDao
import ru.surfstudio.android.demo.network.room.dao.ShortProductModelDao
import ru.surfstudio.android.demo.network.room.models.DetailedProductModel
import ru.surfstudio.android.demo.network.room.models.ShortProductModel

@Database(
    entities = [
        ShortProductModel::class,
        DetailedProductModel::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun shortProductModelDao(): ShortProductModelDao

    abstract fun detailedProductModelDao(): DetailedProductModelDao
}