/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.components.compose.overload.buttons

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.surfstudio.android.demo.components.compose.base.buttons.BaseAppButton
import ru.surfstudio.android.demo.components.compose.overload.texts.Text_Headline_Medium_16
import ru.surfstudio.android.demo.components.compose.overload.texts.Text_Text_Medium_14
import ru.surfstudio.android.demo.components.compose.theme.AppTheme
import ru.surfstudio.android.demo.components.compose.theme.SurfMviDemoTheme

@Composable
private fun ButtonCancel(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    contentPadding: PaddingValues = bigButtonPaddingValues,
    onClick: () -> Unit,
    textContent: @Composable () -> Unit
) = BaseAppButton(
    modifier = modifier,
    isEnabled = isEnabled,
    isLoading = isLoading,
    colors = ButtonDefaults.buttonColors(
        backgroundColor = AppTheme.colors.Red_Light,
        // disable clicks without style changes according to design
        disabledBackgroundColor = AppTheme.colors.Red_Light.takeIf { isLoading }
            ?: AppTheme.colors.Gray_050,
        contentColor = AppTheme.colors.Red,
        disabledContentColor = AppTheme.colors.Steel_Gray_200
    ),
    loadingColor = AppTheme.colors.Red,
    contentPadding = contentPadding,
    onClick = onClick,
    textContent = textContent
)

@Composable
fun ButtonCancelBig(
    text: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit
) = ButtonCancel(
    modifier = modifier.height(46.dp),
    isEnabled = isEnabled,
    isLoading = isLoading,
    contentPadding = bigButtonPaddingValues,
    onClick = onClick,
    textContent = {
        Text_Headline_Medium_16(text = text)
    }
)

@Composable
fun ButtonCancelMedium(
    text: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit
) = ButtonCancel(
    modifier = modifier.height(40.dp),
    isEnabled = isEnabled,
    isLoading = isLoading,
    contentPadding = mediumButtonPaddingValues,
    onClick = onClick,
    textContent = {
        Text_Text_Medium_14(text = text)
    }
)

@Composable
fun ButtonCancelSmall(
    text: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit
) = ButtonCancel(
    modifier = modifier.height(32.dp),
    isEnabled = isEnabled,
    isLoading = isLoading,
    contentPadding = smallButtonPaddingValues,
    onClick = onClick,
    textContent = {
        Text_Text_Medium_14(text = text)
    }
)

@Preview(showBackground = true, device = Devices.PIXEL, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun Preview() {
    SurfMviDemoTheme {
        Surface {
            Column(modifier = Modifier.fillMaxSize()) {
                ButtonCancelBig(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = "Enabled",
                    onClick = {}
                )
                ButtonCancelBig(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    isEnabled = false,
                    text = "Disabled",
                    onClick = {}
                )
                ButtonCancelBig(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    isLoading = true,
                    text = "Loading",
                    onClick = {}
                )

                ButtonCancelMedium(
                    modifier = Modifier
                        .defaultMinSize(minWidth = 98.dp)
                        .padding(16.dp),
                    text = "Enabled",
                    onClick = {}
                )
                ButtonCancelMedium(
                    modifier = Modifier
                        .defaultMinSize(minWidth = 98.dp)
                        .padding(16.dp),
                    isEnabled = false,
                    text = "Disabled",
                    onClick = {}
                )
                ButtonCancelMedium(
                    modifier = Modifier
                        .defaultMinSize(minWidth = 98.dp)
                        .padding(16.dp),
                    isLoading = true,
                    text = "Loading",
                    onClick = {}
                )

                ButtonCancelSmall(
                    modifier = Modifier
                        .defaultMinSize(minWidth = 81.dp)
                        .padding(16.dp),
                    text = "Enabled",
                    onClick = {}
                )
                ButtonCancelSmall(
                    modifier = Modifier
                        .defaultMinSize(minWidth = 81.dp)
                        .padding(16.dp),
                    isEnabled = false,
                    text = "Disabled",
                    onClick = {}
                )
                ButtonCancelSmall(
                    modifier = Modifier
                        .defaultMinSize(minWidth = 81.dp)
                        .padding(16.dp),
                    isLoading = true,
                    text = "Loading",
                    onClick = {}
                )
            }
        }
    }
}