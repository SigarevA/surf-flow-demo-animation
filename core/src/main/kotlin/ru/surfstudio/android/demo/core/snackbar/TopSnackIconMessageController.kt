/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.core.snackbar

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.demo.resources.R as resourcesR

/**
 * Класс, позволяющий отображать уведомления по верхней части текущей активити.
 */
class TopSnackIconMessageController(
    private val activityHolder: ActiveActivityHolder
) : IconMessageController {

    override fun closeSnack() {
        Alerter.hide()
    }

    override fun show(
        stringId: Int,
        actionResId: Int?,
        type: SnackType,
        durationMillis: Long,
        listener: ((view: View) -> Unit)?
    ) {
        val activity = activityHolder.activity ?: error("Activity is null")
        val message = activity.resources.getString(stringId)
        val action = actionResId?.let { activity.resources.getString(actionResId) }
        show(message, action, type, durationMillis, listener)
    }

    override fun show(
        message: String,
        action: String?,
        type: SnackType,
        durationMillis: Long,
        listener: ((view: View) -> Unit)?
    ) {
        Alerter.hide()

        val activity = activityHolder.activity ?: error("Activity is null")
        val backgroundColorTint =
            ContextCompat.getColor(activity as Context, type.backgroundColorRes)
        val backgroundRes = resourcesR.drawable.bg_snack
        val background = ContextCompat
            .getDrawable(activity, backgroundRes)
            ?.apply { DrawableCompat.setTint(this, backgroundColorTint) }

        Alerter.create(activity.findViewById(android.R.id.content)).apply {
            setText(message)
            action?.also { setActionText(action) }
            setDuration(durationMillis)
            listener?.also { setOnClickListener { view -> listener(view) } }
            showIcon(listener == null)
            showButton(listener != null)
            background?.let(::setBackgroundDrawable)
            setIcon(type.iconRes)
            enableSwipeToDismiss()
            show()
        }
    }
}
