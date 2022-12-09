/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.components.compose.base.dialogs

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.surfstudio.android.demo.components.compose.overload.texts.Text_Headline_Regular_16
import ru.surfstudio.android.demo.components.compose.overload.texts.Text_Text_Medium_14
import ru.surfstudio.android.demo.components.compose.overload.texts.Text_Title_2_Medium_20
import ru.surfstudio.android.demo.components.compose.theme.AppTheme
import ru.surfstudio.android.demo.components.compose.theme.SurfMviDemoTheme

/** Base dialog for app */
@Composable
fun BaseAppDialog(
    title: String,
    message: String? = null,
    positiveText: String,
    positiveClicked: (() -> Unit),
    positiveTextColor: Color = AppTheme.colors.Turquoise,
    negativeTextColor: Color = AppTheme.colors.Red,
    negativeText: String? = null,
    negativeClicked: (() -> Unit) = {},
    onDismissRequest: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        DialogContent(
            title = title,
            message = message,
            positiveText = positiveText,
            positiveClicked = positiveClicked,
            positiveTextColor = positiveTextColor,
            negativeTextColor = negativeTextColor,
            negativeText = negativeText,
            negativeClicked = negativeClicked
        )
    }
}

@Composable
private fun DialogContent(
    title: String,
    message: String? = null,
    positiveText: String,
    positiveClicked: (() -> Unit),
    positiveTextColor: Color = AppTheme.colors.Turquoise,
    negativeTextColor: Color = AppTheme.colors.Red,
    negativeText: String? = null,
    negativeClicked: (() -> Unit) = {}
) {
    Surface(
        modifier = Modifier.widthIn(min = 300.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {

            Column(
                modifier = Modifier.padding(
                    top = 19.dp,
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 30.dp
                )
            ) {
                val hasTitle = title.isNotBlank()
                if (hasTitle) {
                    Text_Title_2_Medium_20(text = title)
                }

                message?.let {
                    Text_Headline_Regular_16(
                        modifier = Modifier.padding(top = 16.dp.takeIf { hasTitle } ?: 0.dp),
                        text = message
                    )
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 8.dp, bottom = 8.dp)
            ) {
                negativeText?.let {
                    TextButton(
                        modifier = Modifier.padding(end = 8.dp),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                            disabledElevation = 0.dp
                        ),
                        onClick = negativeClicked
                    ) {
                        Text_Text_Medium_14(
                            text = negativeText.uppercase(),
                            color = negativeTextColor
                        )
                    }
                }
                TextButton(
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        disabledElevation = 0.dp
                    ),
                    onClick = positiveClicked
                ) {
                    Text_Text_Medium_14(
                        text = positiveText.uppercase(),
                        color = positiveTextColor
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun Preview() {
    SurfMviDemoTheme {
        Surface {
            Column {
                DialogContent(
                    title = "Title",
                    message = "Message",
                    positiveClicked = {},
                    positiveText = "Ok",
                    negativeText = "Cancel"
                )
                DialogContent(
                    title = "",
                    message = "No title",
                    positiveClicked = {},
                    positiveText = "Ok",
                    negativeText = "Cancel"
                )
            }
        }
    }
}
