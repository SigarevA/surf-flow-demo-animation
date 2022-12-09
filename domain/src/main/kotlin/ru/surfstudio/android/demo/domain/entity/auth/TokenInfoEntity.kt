/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.domain.entity.auth

import androidx.compose.runtime.Immutable

/**
 * Модель данных для токенов авторизованного пользователя
 *
 * @property accessToken Access-token для доступа к ресурсам сервера для авторизованного пользователя
 * @property refreshToken Refresh-token для обновление access-токена
 */
@Immutable
data class TokenInfoEntity(
    val accessToken: String,
    val refreshToken: String
)