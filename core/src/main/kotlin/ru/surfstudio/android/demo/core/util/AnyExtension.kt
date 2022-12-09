/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.core.util

/** Extension-метод для сравнения `this` с множеством [others]. */
fun Any.isOneOf(vararg others: Any?): Boolean {
    return others.any(::equals)
}

/** Extension-метод для сравнения `this` с множеством [others]. */
fun Any.isNoneOf(vararg others: Any?): Boolean {
    return others.none(::equals)
}