/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.domain.entity.auth

import androidx.compose.runtime.Immutable

/**
 * Модель данных авторизованного пользователя
 */
@Immutable
data class AuthDataEntity(
    val tokenInfo: TokenInfoEntity
)