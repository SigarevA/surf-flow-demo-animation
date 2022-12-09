/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.core.shared.result.provider

/** Сущность, которая предоставляет типизированный результат для `SharedResult`. */
interface SharedResultDataProvider<T : Any> {

    /** Типизированный результат работы. */
    val sharedResultData: T
}