/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.core

/**
 * Object which stores tokens in RAM after the successful auth based on `TokenInfoEntity`.
 */
object TokenInfoProvider {
    var accessToken: String? = null
    var refreshToken: String? = null
}