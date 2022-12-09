/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.test_utils

import android.view.View
import ru.surfstudio.android.demo.core.snackbar.IconMessageController
import ru.surfstudio.android.demo.core.snackbar.SnackType

class TestMessageController : IconMessageController {

    var snackIsShown: Boolean = false

    override fun show(
        message: String,
        action: String?,
        type: SnackType,
        durationMillis: Long,
        listener: ((view: View) -> Unit)?
    ) {
        snackIsShown = true
    }

    override fun show(
        stringId: Int,
        actionResId: Int?,
        type: SnackType,
        durationMillis: Long,
        listener: ((view: View) -> Unit)?
    ) {
        snackIsShown = true
    }

    override fun closeSnack() {
        snackIsShown = false
    }
}