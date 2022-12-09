/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.test.core

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertTrue
import ru.surfstudio.android.demo.core.mvi.mapper.LoadStateType
import ru.surfstudio.android.demo.core.mvi.mapper.RequestMappers
import ru.surfstudio.android.demo.core.util.isOneOf
import ru.surfstudio.android.demo.test_utils.TestErrorHandler
import ru.surfstudio.mvi.mappers.Request
import ru.surfstudio.mvi.mappers.RequestMapper
import ru.surfstudio.mvi.mappers.RequestUi

class RequestErrorMappersTest {

    //region RequestMappers.error.forced
    @Test
    fun `when forced error handler called, error is always handled`() {
        val errorHandler = TestErrorHandler()
        val request = Request.Error<String>(NoSuchElementException())
        val requestUi = RequestUi("123")
        RequestMapper.builder(request, requestUi)
            .handleError(RequestMappers.error.forced(errorHandler))
            .build()
        assertTrue(errorHandler.errorHandled)
    }
    //endregion

    //region RequestMappers.error.loadingBased
    @Test
    fun `when loadingBased error handler called, NonAuthorizedException is always handled`() {
        val errorHandler = TestErrorHandler()
        val request = Request.Error<String>(getTestNonAuthorizedException())
        val requestUi = RequestUi("123", load = LoadStateType.None)
        RequestMapper.builder(request, requestUi)
            .mapLoading { _, _ -> requestUi.load as LoadStateType }
            .handleError(RequestMappers.error.loadingBased(errorHandler))
            .build()
        assertTrue(errorHandler.errorHandled)
    }

    @Test
    fun `when loadingBased error handler called, Error and NoInternet are not handled for other error`() {
        val request = Request.Error<String>(NoSuchElementException())
        listOf(
            LoadStateType.None,
            LoadStateType.SwipeRefreshLoading,
            LoadStateType.Error,
            LoadStateType.NoInternet,
            LoadStateType.Empty,
            LoadStateType.Main,
            LoadStateType.TransparentLoading
        ).forEach {
            val excludedLoadState = it.isOneOf(LoadStateType.Error, LoadStateType.NoInternet)
            val errorHandler = TestErrorHandler()
            val requestUi = RequestUi("123", load = it)
            RequestMapper.builder(request, requestUi)
                .mapLoading { _, _ -> requestUi.load as LoadStateType }
                .handleError(RequestMappers.error.loadingBased(errorHandler))
                .build()
            assertTrue(
                excludedLoadState && !errorHandler.errorHandled ||
                        !excludedLoadState && errorHandler.errorHandled
            )
        }
    }
    //endregion
}