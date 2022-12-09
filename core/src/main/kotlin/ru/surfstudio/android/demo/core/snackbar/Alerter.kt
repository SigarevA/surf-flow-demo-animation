/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.core.snackbar

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.core.view.ViewCompat
import ru.surfstudio.android.demo.core.R
import java.lang.ref.WeakReference

/**
 * Класс, временно прикрепляющий уведомление к контенту текущего контейнера поверх остальных вью.
 */
class Alerter private constructor() {

    /**
     * Ссылка на Alert, для поддержки, конфигурирования
     */
    private var alert: Alert? = null

    /**
     * Получение DecorView контейнера
     *
     * @return Decor View контейнера, из которого был вызван Alerter
     */
    private val containerDecorView: ViewGroup?
        get() = weakReference?.get()

    /**
     * Показывает Alert после его создания
     *
     * @return Экземпляр класса Alert, который может быть изменен или скрыт
     */
    fun show(): Alert? {
        weakReference?.get()?.post {
            containerDecorView?.addView(alert)
        }
        return alert
    }

    /**
     * Добавляет дополнительный отступ у Alert, для отступа от Toolbar
     */
    fun addAdditionalMargin(marginSize: Int): Alerter {
        alert?.addAdditionalMargin(marginSize)
        return this
    }

    /**
     * Устанавливает текст уведомления
     *
     * @param textId id текстового ресурса
     * @return This Alerter
     */
    fun setText(@StringRes textId: Int): Alerter {
        alert?.setText(textId)
        return this
    }

    /**
     * Устанавливает текст уведомления
     *
     * @param text CharSequence текста
     * @return This Alerter
     */
    fun setText(text: CharSequence): Alerter {
        alert?.setText(text)
        return this
    }

    /**
     * Устанавливает текст кнопки уведомления
     *
     * @param text CharSequence текста
     * @return This Alerter
     */
    fun setActionText(text: CharSequence): Alerter {
        alert?.setActionText(text)
        return this
    }

    /**
     * Устанавливает Typeface текста уведомления
     *
     * @param typeface Typeface
     * @return This Alerter
     */
    fun setTextTypeface(typeface: Typeface): Alerter {
        alert?.setTextTypeface(typeface)
        return this
    }

    /**
     * Устанавливает TextAppearance текста уведомления
     *
     * @param textAppearance id ресурса стиля
     * @return This Alerter
     */
    fun setTextAppearance(@StyleRes textAppearance: Int): Alerter {
        alert?.setTextAppearance(textAppearance)
        return this
    }

    /**
     * Устанавливает фоновый Drawable
     *
     * @param drawable Drawable
     * @return This Alerter
     */
    fun setBackgroundDrawable(drawable: Drawable): Alerter {
        alert?.setAlertBackgroundDrawable(drawable)
        return this
    }

    /**
     * Устанавливает фоновый Drawable
     *
     * @param drawableResId Id Drawable ресурса
     * @return This Alerter
     */
    fun setBackgroundResource(@DrawableRes drawableResId: Int): Alerter {
        alert?.setAlertBackgroundResource(drawableResId)
        return this
    }

    /**
     * Устанавливает иконку уведомления
     *
     * @param iconId Id ресурса изображения
     * @return This Alerter
     */
    fun setIcon(@DrawableRes iconId: Int): Alerter {
        alert?.setIcon(iconId)
        return this
    }

    /**
     * Скрывает иконку уведомления
     *
     * @return This Alerter
     */
    fun hideIcon(): Alerter {
        alert?.showIcon = false
        return this
    }

    /**
     * Устанавливает onClickListener для уведомления
     *
     * @param onClickListener The onClickListener for the Alert
     * @return This Alerter
     */
    fun setOnClickListener(onClickListener: View.OnClickListener): Alerter {
        alert?.setOnClickListener(onClickListener)
        return this
    }

    /**
     * Устанавливает продолжительность отображения уведомления
     *
     * @param milliseconds Продолжительность в миллисекундах
     * @return This Alerter
     */
    fun setDuration(milliseconds: Long): Alerter {
        alert?.duration = milliseconds
        return this
    }

    /**
     * Устанавливает следует показывать иконку или нет
     *
     * @param showIcon True чтобы показывать, иначе false
     * @return This Alerter
     */
    fun showIcon(showIcon: Boolean): Alerter {
        alert?.showIcon = showIcon
        return this
    }

    /**
     * Устанавливает следует показывать кнопку или нет
     *
     * @param showButton True чтобы показывать, иначе false
     * @return This Alerter
     */
    fun showButton(showButton: Boolean): Alerter {
        alert?.showButton = showButton
        return this
    }

    /**
     * Включает или выключает бесконечную продолжительность отображения уведомления
     *
     * @param infiniteDuration True если следует включить бесконечное отображение
     * @return This Alerter
     */
    fun enableInfiniteDuration(infiniteDuration: Boolean): Alerter {
        alert?.setEnableInfiniteDuration(infiniteDuration)
        return this
    }

    /**
     * Устанавливает слушатель события появления уведомления на экране
     *
     * @param listener OnShowAlertListener
     * @return This Alerter
     */
    fun setOnShowListener(listener: OnShowAlertListener): Alerter {
        alert?.setOnShowListener(listener)
        return this
    }

    /**
     * Устанавливает слушатель события скрытия уведомления
     *
     * @param listener OnHideAlertListener
     * @return This Alerter
     */
    fun setOnHideListener(listener: OnHideAlertListener): Alerter {
        alert?.onHideListener = listener
        return this
    }

    /**
     * Включает swipe to dismiss
     *
     * @return This Alerter
     */
    fun enableSwipeToDismiss(): Alerter {
        alert?.enableSwipeToDismiss()
        return this
    }

    /**
     * Включает или выключает вибрационный отклик на появление уведомления
     *
     * @param enable True для включения, False для выключения
     * @return This Alerter
     */
    fun enableVibration(enable: Boolean): Alerter {
        alert?.setVibrationEnabled(enable)
        return this
    }

    /**
     * Выключает обработку прикосновений вне области уведомления
     *
     * @return This Alerter
     */
    fun disableOutsideTouch(): Alerter {
        alert?.disableOutsideTouch()
        return this
    }

    /**
     * Устанавливает может ли пользователь смахнуть уведомление
     *
     * @param dismissible true если пользователь может смахнуть уведомление
     * @return This Alerter
     */
    fun setDismissible(dismissible: Boolean): Alerter {
        alert?.isDismissible = dismissible
        return this
    }

    /**
     * Устанавливает анимацию появления уведомления
     *
     * @param animation анимация появления уведомления
     * @return This Alerter
     */
    fun setEnterAnimation(@AnimRes animation: Int): Alerter {
        alert?.enterAnimation = AnimationUtils.loadAnimation(alert?.context, animation)
        return this
    }

    /**
     * Устанавливает анимацию скрытия уведомления
     *
     * @param animation анимация скрытия уведомления
     * @return This Alerter
     */
    fun setExitAnimation(@AnimRes animation: Int): Alerter {
        alert?.exitAnimation = AnimationUtils.loadAnimation(alert?.context, animation)
        return this
    }

    /**
     * Создает WeakReference для ссылки на контейнер
     *
     * @param container The calling container
     */
    private fun setContainer(container: ViewGroup) {
        weakReference = WeakReference(container)
    }

    companion object {

        private var weakReference: WeakReference<ViewGroup>? = null

        /**
         * Создает уведомление и сохраняет ссылку на контейнер, из которого был вызван метод
         *
         * @param container Контейнер, в котором будет показано уведомление
         * @return This Alerter
         */
        @JvmStatic
        fun create(container: ViewGroup): Alerter {

            val alerter = Alerter()

            // Скрывает текущее уведомление, если есть активное
            clearCurrent(container)

            alerter.setContainer(container)
            alerter.alert = Alert(container.context)
            return alerter
        }

        /**
         * Очищает отображаемое уведомление если есть активное
         *
         * @param container Текущий контейнер
         */
        @JvmStatic
        fun clearCurrent(container: ViewGroup) {
            container.post {
                // Находим все вью уведомлений в родительском макете
                for (i in 0..container.childCount) {
                    val childView =
                        if (container.getChildAt(i) is Alert) container.getChildAt(i) as Alert else null
                    if (childView != null && childView.windowToken != null) {
                        ViewCompat.animate(childView).alpha(0f)
                            .withEndAction(getRemoveViewRunnable(childView))
                    }
                }
            }
        }

        /**
         * Скрывает отображаемое уведомление, если такое есть
         */
        @JvmStatic
        fun hide() {
            weakReference?.get()?.let {
                clearCurrent(it)
            }
        }

        /**
         * Проверяет отображается ли в данный момент уведомление
         *
         * @return True если уведомление отображается
         */
        @JvmStatic
        val isShowing: Boolean
            get() {
                var isShowing = false

                weakReference?.get()?.let {
                    isShowing = it.findViewById<View>(R.id.llAlertBackground) != null
                }

                return isShowing
            }

        private fun getRemoveViewRunnable(childView: Alert?): Runnable {
            return Runnable {
                childView?.let {
                    (childView.parent as? ViewGroup)?.removeView(childView)
                }
            }
        }
    }
}
