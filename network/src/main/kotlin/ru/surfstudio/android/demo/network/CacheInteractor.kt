/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import ru.surfstudio.android.demo.core.network.NoInternetException
import ru.surfstudio.android.demo.network.room.models.base.IModel

/** Base interactor which handles network with cache requests */
interface CacheInteractor {

    fun <Model, Entity> getCachedData(
        fromCache: Boolean = true,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        cacheRequest: suspend () -> Model?,
        networkRequest: suspend () -> Entity,
        updateCacheRequest: suspend (Entity) -> Unit
    ): Flow<Entity> where Model : IModel, Model : Transformable<Entity> =
        if (fromCache) {
            flow {
                cacheRequest()?.transform()?.let { cache -> emit(cache) }
                delay(2000L)
                emit(networkRequest().also { updateCacheRequest(it) })
            }.catch {
                if (it is NoInternetException) {
                    throw it // propagate exception in order to handle it further
                }
            }.flowOn(dispatcher)
        } else {
            flow {
                emit(networkRequest().also { updateCacheRequest(it) })
            }.flowOn(dispatcher)
        }
}