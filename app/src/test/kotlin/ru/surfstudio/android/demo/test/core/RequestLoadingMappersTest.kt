/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.test.core

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import ru.surfstudio.android.demo.core.mvi.mapper.LoadStateType
import ru.surfstudio.android.demo.core.mvi.mapper.RequestMappers
import ru.surfstudio.android.demo.core.network.NoInternetException
import ru.surfstudio.mvi.mappers.Request
import ru.surfstudio.mvi.mappers.RequestMapper
import ru.surfstudio.mvi.mappers.RequestUi

class RequestLoadingMappersTest {

    //region RequestMappers.loading.simple()
    @Test
    fun `test simple loading mapper stores isLoading attribute`() {
        run {
            val request = Request.Success("456")
            val newRequestUi = RequestMapper.builder(request)
                .mapLoading(RequestMappers.loading.simple())
                .build()
            assertFalse(newRequestUi.isLoading)
        }

        run {
            val request = Request.Loading<String>()
            val newRequestUi = RequestMapper.builder(request)
                .mapLoading(RequestMappers.loading.simple())
                .build()
            assertTrue(newRequestUi.isLoading)
        }
    }
    //endregion

    //region RequestMappers.loading.default()
    @Test
    fun `test default loading mapper returns SwipeRefreshLoading when loading, isSwr and hasData`() {
        val request = Request.Loading<String>()
        val requestUi = RequestUi("123")
        val newRequestUi = RequestMapper.builder(request, requestUi)
            .mapLoading(RequestMappers.loading.default(isSwr = true))
            .build()
        assertTrue(newRequestUi.load == LoadStateType.SwipeRefreshLoading)
    }

    @Test
    fun `test default loading mapper returns TransparentLoading when loading and hasData`() {
        val request = Request.Loading<String>()
        val requestUi = RequestUi("123")
        val newRequestUi = RequestMapper.builder(request, requestUi)
            .mapLoading(RequestMappers.loading.default())
            .build()
        assertTrue(newRequestUi.load == LoadStateType.TransparentLoading)
    }

    @Test
    fun `test default loading mapper returns None when hasError and hasData`() {
        val request = Request.Error<String>(NoSuchElementException())
        val requestUi = RequestUi("123")
        val newRequestUi = RequestMapper.builder(request, requestUi)
            .mapLoading(RequestMappers.loading.default())
            .build()
        assertTrue(newRequestUi.load == LoadStateType.None)
    }

    @Test
    fun `test default loading mapper returns Main when isLoading and no data`() {
        val request = Request.Loading<String>()
        val requestUi = RequestUi<String>()
        val newRequestUi = RequestMapper.builder(request, requestUi)
            .mapLoading(RequestMappers.loading.default())
            .build()
        assertTrue(newRequestUi.load == LoadStateType.Main)
    }

    @Test
    fun `test default loading mapper returns Error when hasError and no data`() {
        val request = Request.Error<String>(NoSuchElementException())
        val requestUi = RequestUi<String>()
        val newRequestUi = RequestMapper.builder(request, requestUi)
            .mapLoading(RequestMappers.loading.default())
            .build()
        assertTrue(newRequestUi.load == LoadStateType.Error)
    }

    @Test
    fun `test default loading mapper returns NoInternet when hasError and error is NoInternetException and no data`() {
        val request = Request.Error<String>(NoInternetException(RuntimeException()))
        val requestUi = RequestUi<String>()
        val newRequestUi = RequestMapper.builder(request, requestUi)
            .mapLoading(RequestMappers.loading.default())
            .build()
        assertTrue(newRequestUi.load == LoadStateType.NoInternet)
    }

    @Test
    fun `test default loading mapper returns Empty when succeed and no data`() {
        val request = Request.Success(listOf<String>())
        val newRequestUi = RequestMapper.builder(request)
            .mapLoading(RequestMappers.loading.default(showEmptyState = true))
            .build()
        assertTrue(newRequestUi.load == LoadStateType.Empty)
    }
    //endregion

    //region RequestMappers.loading.mainOrNone()
    @Test
    fun `test mainOrNone loading mapper returns SwipeRefreshLoading for loading and isSwr`() {
        val request = Request.Loading<String>()
        val newRequestUi = RequestMapper.builder(request)
            .mapLoading(RequestMappers.loading.mainOrNone(true))
            .build()
        assertTrue(newRequestUi.load == LoadStateType.SwipeRefreshLoading)
    }

    @Test
    fun `test mainOrNone loading mapper returns Main for loading and not isSwr`() {
        val request = Request.Loading<String>()
        val newRequestUi = RequestMapper.builder(request)
            .mapLoading(RequestMappers.loading.mainOrNone())
            .build()
        assertTrue(newRequestUi.load == LoadStateType.Main)
    }

    @Test
    fun `test mainOrNone loading mapper returns None for not loading`() {
        val request = Request.Error<String>(NoSuchElementException())
        val newRequestUi = RequestMapper.builder(request)
            .mapLoading(RequestMappers.loading.mainOrNone())
            .build()
        assertTrue(newRequestUi.load == LoadStateType.None)
    }
    //endregion

    //region RequestMappers.loading.transparentOrNone()
    @Test
    fun `test transparentOrNone loading mapper returns SwipeRefreshLoading for loading and isSwr`() {
        val request = Request.Loading<String>()
        val newRequestUi = RequestMapper.builder(request)
            .mapLoading(RequestMappers.loading.transparentOrNone(true))
            .build()
        assertTrue(newRequestUi.load == LoadStateType.SwipeRefreshLoading)
    }

    @Test
    fun `test transparentOrNone loading mapper returns TransparentLoading for loading and not isSwr`() {
        val request = Request.Loading<String>()
        val newRequestUi = RequestMapper.builder(request)
            .mapLoading(RequestMappers.loading.transparentOrNone())
            .build()
        assertTrue(newRequestUi.load == LoadStateType.TransparentLoading)
    }

    @Test
    fun `test transparentOrNone loading mapper returns None for not loading`() {
        val request = Request.Error<String>(NoSuchElementException())
        val newRequestUi = RequestMapper.builder(request)
            .mapLoading(RequestMappers.loading.transparentOrNone())
            .build()
        assertTrue(newRequestUi.load == LoadStateType.None)
    }
    //endregion
}