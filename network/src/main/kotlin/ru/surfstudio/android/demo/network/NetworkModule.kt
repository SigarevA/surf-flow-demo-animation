/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.surfstudio.android.demo.core.Constants.okhttpTag
import ru.surfstudio.android.demo.debug.interactor.DebugSettingsInteractor
import ru.surfstudio.android.demo.domain.BuildConfig
import ru.surfstudio.android.demo.domain.ServerType
import ru.surfstudio.android.demo.network.adapter.NetworkResponseAdapterFactory
import ru.surfstudio.android.demo.network.session.RetryInterceptor
import ru.surfstudio.android.demo.network.session.ServiceInterceptor
import ru.surfstudio.android.logger.Logger
import java.util.concurrent.TimeUnit

private const val NETWORK_TIMEOUT = 30L // sec
private const val PROD_URL = "https://dev3.bethowen.ru/api/surf/v1/"
private const val MOCK_URL = "https://r2.mocker.surfstudio.ru/bethowen/"

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        retryInterceptor: RetryInterceptor,
        serviceInterceptor: ServiceInterceptor,
        @ApplicationContext appContext: Context
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
                readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
                writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)

                addInterceptor(retryInterceptor)
                addInterceptor(serviceInterceptor)
                addInterceptor(ChuckerInterceptor.Builder(appContext).build())
                addInterceptor(loggingInterceptor)
            }.build()
    }

    @Provides
    fun provideDebugSettingsInteractor(
        @ApplicationContext appContext: Context
    ): DebugSettingsInteractor {
        return DebugSettingsInteractor(appContext)
    }

    @Provides
    @BaseUrl
    fun provideBaseUrl(debugSettingsInteractor: DebugSettingsInteractor): String {
        return if (!BuildConfig.DEBUG) {
            PROD_URL
        } else when (debugSettingsInteractor.getServerTypeBlocking()) {
            ServerType.PROD -> PROD_URL
            ServerType.MOCK -> MOCK_URL
        }
    }

    @Provides
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        @BaseUrl baseUrl: String,
        json: Json
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addCallAdapterFactory(NetworkResponseAdapterFactory(json))
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message -> Logger.d(okhttpTag, message) }
            .apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.BASIC
                }
            }
    }
}