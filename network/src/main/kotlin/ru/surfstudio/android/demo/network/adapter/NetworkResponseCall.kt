/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network.adapter

import kotlinx.serialization.json.Json
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import ru.surfstudio.android.demo.core.network.NoInternetException
import java.net.UnknownHostException

internal class NetworkResponseCall<S : Any>(
    private val delegate: Call<S>,
    private val json: Json
) : Call<S> {

    override fun enqueue(callback: Callback<S>) {
        return delegate.enqueue(
            object : Callback<S> {
                override fun onResponse(call: Call<S>, response: Response<S>) {
                    if (response.isSuccessful) {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(response.body())
                        )
                    } else {
                        callback.onFailure(
                            this@NetworkResponseCall,
                            NetworkErrorConverter.convert(HttpException(response), json)
                        )
                    }
                }

                override fun onFailure(call: Call<S>, throwable: Throwable) {
                    val castedError = if (throwable is UnknownHostException) {
                        NoInternetException(cause = throwable)
                    } else {
                        throwable
                    }
                    callback.onFailure(this@NetworkResponseCall, castedError)
                }
            }
        )
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = NetworkResponseCall(delegate.clone(), json)

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<S> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
    }

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}
