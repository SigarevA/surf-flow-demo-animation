/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.generated.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.surfstudio.android.demo.network.generated.api.ProductsApi

@Module
@InstallIn(SingletonComponent::class)
internal object ProductsModule {

    @Provides
    fun provideAuthApi(retrofit: Retrofit): ProductsApi {
        return retrofit.create(ProductsApi::class.java)
    }
}