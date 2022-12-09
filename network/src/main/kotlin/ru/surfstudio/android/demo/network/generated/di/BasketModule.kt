/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.generated.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.surfstudio.android.demo.network.generated.api.BasketApi

@Module
@InstallIn(SingletonComponent::class)
internal object BasketModule {

    @Provides
    fun provideBasketApi(retrofit: Retrofit): BasketApi {
        return retrofit.create(BasketApi::class.java)
    }
}