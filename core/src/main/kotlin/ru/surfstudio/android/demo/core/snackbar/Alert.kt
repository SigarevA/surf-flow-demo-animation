/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.core.snackbar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import ru.surfstudio.android.demo.core.R
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.demo.resources.R as resourcesR

typealias OnHideAlertListener = () -> Unit
typealias OnShowAlertListener = () -> Unit

private const val EMPTY_RESOURCE = 0

/**
 * Custom View для отображения уведомления
 */
class Alert @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle),
    View.OnClickListener,
    Animation.AnimationListener,
    SwipeDismissTouchListener.DismissCallbacks {

    /**
     * True, если следует показывать иконку уведомления, иначе false
     */
    var showIcon: Boolean = true

    /**
     * True, если следует показывать кнопку уведомления, иначе false
     */
    var showButton: Boolean = false

    /**
     * True, если пользователь может смахнуть уведомление
     */
    var isDismissible = false

    private var onShowListener: OnShowAlertListener? = null
    internal var onHideListener: OnHideAlertListener? = null

    internal var enterAnimation: Animation =
        AnimationUtils.loadAnimation(context, resourcesR.anim.alerter_slide_in_from_top)
    internal var exitAnimation: Animation =
        AnimationUtils.loadAnimation(context, resourcesR.anim.alerter_slide_out_to_top)

    internal var duration = DISPLAY_TIME_IN_SECONDS

    @DrawableRes
    private var iconResId: Int = EMPTY_RESOURCE
    private var enableInfiniteDuration: Boolean = false

    private var runningAnimation: Runnable? = null

    /**
     * Флаг для включения/выключения вибрационного отклика при отображении уведомления
     */
    private var vibrationEnabled = false

    /**
     * Flag to ensure we only set the margins once
     */
    private var marginSet: Boolean = false

    private val llAlertBackground: ConstraintLayout
    private val flClickShield: LinearLayout
    private val tvText: TextView
    private val btnReload: Button

    init {
        inflate(context, R.layout.alerter_alert_view, this)

        isHapticFeedbackEnabled = true
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        ViewCompat.setTranslationZ(this, Float.MAX_VALUE)

        llAlertBackground = findViewById(R.id.llAlertBackground)
        flClickShield = findViewById(R.id.flClickShield)
        tvText = findViewById(R.id.tvText)
        btnReload = findViewById(R.id.btnReload)

        llAlertBackground.setOnClickListener(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        enterAnimation.setAnimationListener(this)

        // Устанавливает анимацию появления, которая сработает при добавлении View к Window
        animation = enterAnimation
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (!marginSet) {
            marginSet = true

            // Add a negative top margin to compensate for overshoot enter animation
            (layoutParams as MarginLayoutParams).topMargin =
                (-32 * resources.displayMetrics.density).toInt() // dip

            // Check for Cutout
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val notchHeight = rootWindowInsets?.displayCutout?.safeInsetTop ?: 0
                if (notchHeight > 0) {
                    llAlertBackground.setTopMargin(notchHeight / 2)
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    // Освобождаем ресурсы после того как вью откреплено
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        enterAnimation.setAnimationListener(null)
    }

    /* Override Methods */

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.performClick()
        return super.onTouchEvent(event)
    }

    override fun onClick(v: View) {
        if (isDismissible) {
            hide()
        }
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        btnReload.setOnClickListener(listener)
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        children.forEach { it.visibility = visibility }
    }

    /* Interface Method Implementations */

    override fun onAnimationStart(animation: Animation) {
        if (!isInEditMode) {
            visibility = View.VISIBLE

            if (vibrationEnabled) {
                performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            }

            tvText.post {
                tvText.setCompoundDrawablesWithIntrinsicBounds(
                    iconResId.takeIf { showIcon && tvText.lineCount == 1 }
                        ?: EMPTY_RESOURCE,
                    EMPTY_RESOURCE,
                    EMPTY_RESOURCE,
                    EMPTY_RESOURCE
                )
                tvText.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    endToEnd = if (!showButton) ConstraintLayout.LayoutParams.PARENT_ID else -1
                }
            }
            btnReload.isVisible = showButton
        }
    }

    override fun onAnimationEnd(animation: Animation) {
        onShowListener?.invoke()

        startHideAnimation()
    }

    private fun startHideAnimation() {
        // Запускает Handler для очистки уведомления
        if (!enableInfiniteDuration) {
            runningAnimation = Runnable { hide() }

            postDelayed(runningAnimation, duration)
        }
    }

    private fun View.setTopMargin(topMarginPx: Int) {
        val params = layoutParams as? MarginLayoutParams
        params?.topMargin = topMarginPx
    }

    override fun onAnimationRepeat(animation: Animation) {
        // Ignore
    }

    /* Clean Up Methods */

    /**
     * Скрывает уведомление, которое в данный момент отображается
     */
    private fun hide() {
        try {
            exitAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    llAlertBackground.setOnClickListener(null)
                    llAlertBackground.isClickable = false
                }

                override fun onAnimationEnd(animation: Animation) {
                    removeFromParent()
                }

                override fun onAnimationRepeat(animation: Animation) {
                    // Ignore
                }
            })

            startAnimation(exitAnimation)
        } catch (ex: Exception) {
            Logger.e(javaClass.simpleName, Log.getStackTraceString(ex))
        }
    }

    /**
     * Удаляет вью из родительской ViewGroup
     */
    internal fun removeFromParent() {
        clearAnimation()
        visibility = View.GONE

        postDelayed(
            object : Runnable {
                override fun run() {
                    try {
                        if (parent != null) {
                            try {
                                (parent as ViewGroup).removeView(this@Alert)

                                onHideListener?.invoke()
                            } catch (ex: Exception) {
                                Logger.e(javaClass.simpleName, "Cannot remove from parent layout")
                            }
                        }
                    } catch (ex: Exception) {
                        Logger.e(javaClass.simpleName, Log.getStackTraceString(ex))
                    }
                }
            },
            CLEAN_UP_DELAY_MILLIS.toLong()
        )
    }

    /* Setters and Getters */

    /**
     * Устанавливает цвет фона уведомления
     *
     * @param color The qualified colour integer
     */
    fun setAlertBackgroundColor(@ColorInt color: Int) {
        llAlertBackground.setBackgroundColor(color)
    }

    /**
     * Устанавливает фоновый Drawable уведомления
     *
     * @param resource id drawable ресурса
     */
    fun setAlertBackgroundResource(@DrawableRes resource: Int) {
        llAlertBackground.setBackgroundResource(resource)
    }

    /**
     * Устанавливает фоновый Drawable уведомления
     *
     * @param drawable устанавливаемый drawable
     */
    fun setAlertBackgroundDrawable(drawable: Drawable) {
        llAlertBackground.background = drawable
    }

    /**
     * Устанавливает текст уведомления
     *
     * @param textId id строкового ресурса
     */
    fun setText(@StringRes textId: Int) {
        setText(context.getString(textId))
    }

    /**
     * Выключает обработку прикосновений на время пока уведомление отображается
     */
    fun disableOutsideTouch() {
        flClickShield.isClickable = true
    }

    /**
     * Устанавливает typeface текста
     *
     * @param typeface typeface
     */
    fun setTextTypeface(typeface: Typeface) {
        tvText.typeface = typeface
    }

    /**
     * Устанавливает текст уведомления
     *
     * @param text CharSequence текста
     */
    fun setText(text: CharSequence) {
        if (text.isNotBlank()) {
            tvText.visibility = View.VISIBLE
            tvText.text = text
        }
    }

    /**
     * Устанавливает текст кнопки уведомления
     *
     * @param text CharSequence текста
     */
    fun setActionText(text: CharSequence) {
        if (text.isNotBlank()) {
            btnReload.text = text
        }
    }

    /**
     * Устанавливает TextAppearance текста уведомления
     *
     * @param textAppearance id ресурса стиля
     */
    @Suppress("DEPRECATION")
    fun setTextAppearance(@StyleRes textAppearance: Int) {
        tvText.setTextAppearance(textAppearance)
    }

    /**
     * Устанавливает иконку уведомления
     *
     * @param iconId id drawable ресурса
     */
    fun setIcon(@DrawableRes iconId: Int) {
        iconResId = iconId
    }

    /**
     * Включает возможность скрывать уведомление смахиванием
     */
    fun enableSwipeToDismiss() {
        llAlertBackground.let {
            it.setOnTouchListener(
                SwipeDismissTouchListener(
                    it,
                    object : SwipeDismissTouchListener.DismissCallbacks {
                        override fun canDismiss(): Boolean {
                            return true
                        }

                        override fun onDismiss(view: View) {
                            removeFromParent()
                        }

                        override fun onTouch(view: View, touch: Boolean) {
                            // Ignore
                        }
                    }
                )
            )
        }
    }

    /**
     * Включает или выключает бесконечную продолжительность отображения уведомления
     *
     * @param enableInfiniteDuration True если следует включить бесконечное отображение
     */
    fun setEnableInfiniteDuration(enableInfiniteDuration: Boolean) {
        this.enableInfiniteDuration = enableInfiniteDuration
    }

    /**
     * Устанавливает слушатель события появления уведомления на экране
     *
     * @param listener OnShowAlertListener
     */
    fun setOnShowListener(listener: OnShowAlertListener) {
        this.onShowListener = listener
    }

    /**
     * Включает или выключает вибрационный отклик на появление уведомления
     *
     * @param vibrationEnabled True для включения, False для выключения
     */
    fun setVibrationEnabled(vibrationEnabled: Boolean) {
        this.vibrationEnabled = vibrationEnabled
    }

    /**
     * Добавляет дополнительный отступ на размер тулбара
     */
    fun addAdditionalMargin(marginSize: Int) {
        setTopMargin(marginSize)
    }

    override fun canDismiss(): Boolean {
        return isDismissible
    }

    override fun onDismiss(view: View) {
        flClickShield.removeView(llAlertBackground)
    }

    override fun onTouch(view: View, touch: Boolean) {
        if (touch) {
            removeCallbacks(runningAnimation)
        } else {
            startHideAnimation()
        }
    }

    companion object {

        /**
         * Продолжительность отображения уведомления на экране в миллисекундах
         */
        private const val DISPLAY_TIME_IN_SECONDS: Long = 3000

        private const val CLEAN_UP_DELAY_MILLIS = 100
    }
}
