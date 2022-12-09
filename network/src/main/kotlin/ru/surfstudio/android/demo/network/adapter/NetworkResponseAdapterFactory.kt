/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.adapter

import kotlinx.serialization.json.Json
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/** Implements custom error parsing for all requests */
internal class NetworkResponseAdapterFactory(private val json: Json) : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        // suspend functions wrap the response type in `Call`
        if (Call::class.java != getRawType(returnType)) {
            return null
        }

        // check first that the return type is `ParameterizedType`
        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<*>"
        }

        // get the response type inside the `Call` type
        val responseType = getParameterUpperBound(0, returnType)

        return NetworkResponseAdapter<Any>(responseType, json)
    }

    private class NetworkResponseAdapter<S : Any>(
        private val successType: Type,
        private val json: Json
    ) : CallAdapter<S, Call<S>> {

        override fun responseType(): Type = successType

        override fun adapt(call: Call<S>): Call<S> = NetworkResponseCall(call, json)
    }
}
