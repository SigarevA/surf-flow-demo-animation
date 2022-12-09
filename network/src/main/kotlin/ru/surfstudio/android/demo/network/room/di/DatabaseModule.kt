/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.room.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import ru.surfstudio.android.demo.network.room.database.AppDatabase
import ru.surfstudio.android.demo.network.room.database.AppEncryptedDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    /**
     * Sample encryption key.
     * Can be generated initially, must be unique for each app.
     * Advanced key encryption sample https://bit.ly/3DCEBW6
     */
    private const val password = "034619571434072081789f712fb2bd9c"

    @Provides
    @Singleton
    fun provideAppEncryptedDatabase(@ApplicationContext context: Context): AppEncryptedDatabase {
        val passphrase = SQLiteDatabase.getBytes(password.toCharArray())
        return Room
            .databaseBuilder(
                context,
                AppEncryptedDatabase::class.java,
                "${AppEncryptedDatabase::class.qualifiedName}.db"
            )
            .fallbackToDestructiveMigration()
            .openHelperFactory(SupportFactory(passphrase))
            .build()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(
                context,
                AppDatabase::class.java,
                "${AppDatabase::class.qualifiedName}.db"
            )
            .fallbackToDestructiveMigration()
            .build()
    }
}