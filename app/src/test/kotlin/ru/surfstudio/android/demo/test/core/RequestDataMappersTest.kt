/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.test.core

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertTrue
import ru.surfstudio.android.demo.core.mvi.mapper.RequestMappers
import ru.surfstudio.mvi.mappers.Request
import ru.surfstudio.mvi.mappers.RequestMapper
import ru.surfstudio.mvi.mappers.RequestUi

class RequestDataMappersTest {

    //region RequestMappers.data.single()
    @Test
    fun `when single data mapper gets any error, should return null data`() {
        val request = Request.Error<String>(NoSuchElementException())
        val requestUi = RequestUi("123")
        val newRequestUi = RequestMapper.builder(request, requestUi)
            .mapData(RequestMappers.data.single())
            .build()
        // ignore old data
        assertTrue(newRequestUi.data == null)
        assertTrue(newRequestUi.hasError)
    }

    @Test
    fun `when single data mapper succeeds, should return new data`() {
        val request = Request.Success("456")
        val requestUi = RequestUi("123")
        val newRequestUi = RequestMapper.builder(request, requestUi)
            .mapData(RequestMappers.data.single())
            .build()
        assertTrue(newRequestUi.data == "456")
    }
    //endregion

    //region RequestMappers.data.default()
    @Test
    fun `when default data mapper gets NonAuthorizedException error, should return null data`() {
        val request = Request.Error<String>(getTestNonAuthorizedException())
        val requestUi = RequestUi("123")
        val newRequestUi = RequestMapper.builder(request, requestUi)
            .mapData(RequestMappers.data.default())
            .build()
        // ignore old data
        assertTrue(newRequestUi.data == null)
        assertTrue(newRequestUi.hasError)
    }

    @Test
    fun `when default data mapper gets other error, should return previous data`() {
        val request = Request.Error<String>(NoSuchElementException())
        val requestUi = RequestUi("123")
        val newRequestUi = RequestMapper.builder(request, requestUi)
            .mapData(RequestMappers.data.default())
            .build()
        assertTrue(newRequestUi.data == "123")
        assertTrue(newRequestUi.hasError)
    }

    @Test
    fun `when default data mapper succeeds, should return new data`() {
        val request = Request.Success("456")
        val requestUi = RequestUi("123")
        val newRequestUi = RequestMapper.builder(request, requestUi)
            .mapData(RequestMappers.data.default())
            .build()
        assertTrue(newRequestUi.data == "456")
    }
    //endregion

    //region RequestMappers.data.keepUntilError()
    @Test
    fun `when keepUntilError data mapper gets any error, should return null data`() {
        val request = Request.Error<String>(NoSuchElementException())
        val requestUi = RequestUi("123")
        val newRequestUi = RequestMapper.builder(request, requestUi)
            .mapData(RequestMappers.data.keepUntilError())
            .build()
        // ignore old data
        assertTrue(newRequestUi.data == null)
        assertTrue(newRequestUi.hasError)
    }

    @Test
    fun `when keepUntilError data mapper succeeds, should return new data`() {
        val request = Request.Success("456")
        val requestUi = RequestUi("123")
        val newRequestUi = RequestMapper.builder(request, requestUi)
            .mapData(RequestMappers.data.keepUntilError())
            .build()
        assertTrue(newRequestUi.data == "456")
    }
    //endregion
}