/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.core.snackbar

import android.view.View
import androidx.annotation.StringRes
import ru.surfstudio.android.demo.resources.R as resourcesR

/**
 * Интерфейс контроллера отображения сообщений с изображением
 */
interface IconMessageController {

    /** Показывает уведомление с заданными параметрами */
    fun show(
        message: String,
        action: String? = null,
        type: SnackType = SnackType.SUCCESS,
        durationMillis: Long = DEFAULT_SNACK_DURATION,
        listener: ((view: View) -> Unit)? = null
    )

    /** Показывает уведомление с заданными параметрами */
    fun show(
        @StringRes stringId: Int,
        @StringRes actionResId: Int? = null,
        type: SnackType = SnackType.SUCCESS,
        durationMillis: Long = DEFAULT_SNACK_DURATION,
        listener: ((view: View) -> Unit)? = null
    )

    /** Показ сообщения об ошибке с настройками по умолчанию */
    fun showErrorMessage(@StringRes message: Int) {
        show(message, type = SnackType.ERROR)
    }

    /** Показ сообщения об ошибке с настройками по умолчанию */
    fun showErrorMessage(message: String) {
        show(message, type = SnackType.ERROR)
    }

    /** Закрывает отображаемое в данный момент уведомление */
    fun closeSnack()

    companion object {
        private const val DEFAULT_SNACK_DURATION: Long = 3000
    }
}

enum class SnackType {
    SUCCESS,
    ERROR;

    val backgroundColorRes: Int
        get() = when (this) {
            SUCCESS -> resourcesR.color.succeed_color_snackbar
            ERROR -> resourcesR.color.error_color_snackbar
        }

    val iconRes: Int
        get() = when (this) {
            SUCCESS -> resourcesR.drawable.ic_snack_succeed
            ERROR -> resourcesR.drawable.ic_snack_error
        }
}
