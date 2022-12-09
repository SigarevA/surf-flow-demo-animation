/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.network

/** Transforms response Entry to domain Entity */
internal fun interface Transformable<T> {
    fun transform(): T
}

/** Extension-метод для трансформации коллекции сервисных моделей данных в доменные. */
internal fun <R : Any?, T : Transformable<R>> List<T>?.transformCollection(): List<R> {
    return this?.map { it.transform() }.orEmpty()
}