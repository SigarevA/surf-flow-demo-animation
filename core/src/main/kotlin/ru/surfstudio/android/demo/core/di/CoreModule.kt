/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.surfstudio.android.demo.core.AppStatics
import ru.surfstudio.android.demo.core.ScreenDispatcher
import ru.surfstudio.android.demo.core.ScreenLoadOnStart
import ru.surfstudio.android.demo.core.mvi.error.ErrorHandlerImpl
import ru.surfstudio.android.demo.core.shared.result.SharedResultBus
import ru.surfstudio.android.demo.core.shared.result.impl.SharedResultBusImpl
import ru.surfstudio.android.demo.core.snackbar.IconMessageController
import ru.surfstudio.android.demo.core.snackbar.TopSnackIconMessageController
import ru.surfstudio.mvi.mappers.handler.ErrorHandler

@Module
@InstallIn(SingletonComponent::class)
internal object CoreModule {

    @Provides
    fun provideMessageController(): IconMessageController {
        return TopSnackIconMessageController(AppStatics.activityHolder)
    }

    @Provides
    fun providesErrorHandler(
        messageController: IconMessageController,
        @ApplicationContext context: Context
    ): ErrorHandler {
        return ErrorHandlerImpl(messageController, context)
    }

    @Provides
    fun provideSharedResultBus(): SharedResultBus = SharedResultBusImpl()

    @Provides
    @ScreenDispatcher
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @ScreenLoadOnStart
    fun provideLoadOnStart(): Boolean = true
}