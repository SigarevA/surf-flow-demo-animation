/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.surfstudio.android.demo.network.room.dao.TokenInfoModelDao
import ru.surfstudio.android.demo.network.room.models.TokenInfoModel

@Database(
    entities = [
        TokenInfoModel::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppEncryptedDatabase : RoomDatabase() {

    abstract fun tokenInfoModelDao(): TokenInfoModelDao
}