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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.surfstudio.android.demo.components.compose.base.buttons.BaseAppButton
import ru.surfstudio.android.demo.components.compose.overload.texts.Text_Headline_Medium_16
import ru.surfstudio.android.demo.components.compose.overload.texts.Text_Text_Medium_14
import ru.surfstudio.android.demo.components.compose.theme.AppTheme
import ru.surfstudio.android.demo.components.compose.theme.SurfMviDemoTheme

@Composable
private fun ButtonMain(
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
        backgroundColor = AppTheme.colors.Turquoise,
        // disable clicks without style changes according to design
        disabledBackgroundColor = AppTheme.colors.Turquoise.takeIf { isLoading }
            ?: AppTheme.colors.Steel_Gray_150,
        contentColor = Color.White,
        disabledContentColor = Color.White
    ),
    loadingColor = Color.White,
    contentPadding = contentPadding,
    onClick = onClick,
    textContent = textContent
)

@Composable
fun ButtonMainBig(
    text: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit
) = ButtonMain(
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
fun ButtonMainMedium(
    text: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit
) = ButtonMain(
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
fun ButtonMainSmall(
    text: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit
) = ButtonMain(
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
                ButtonMainBig(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = "Enabled",
                    onClick = {}
                )
                ButtonMainBig(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    isEnabled = false,
                    text = "Disabled",
                    onClick = {}
                )
                ButtonMainBig(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    isLoading = true,
                    text = "Loading",
                    onClick = {}
                )

                ButtonMainMedium(
                    modifier = Modifier
                        .defaultMinSize(minWidth = 98.dp)
                        .padding(16.dp),
                    text = "Enabled",
                    onClick = {}
                )
                ButtonMainMedium(
                    modifier = Modifier
                        .defaultMinSize(minWidth = 98.dp)
                        .padding(16.dp),
                    isEnabled = false,
                    text = "Disabled",
                    onClick = {}
                )
                ButtonMainMedium(
                    modifier = Modifier
                        .defaultMinSize(minWidth = 98.dp)
                        .padding(16.dp),
                    isLoading = true,
                    text = "Loading",
                    onClick = {}
                )

                ButtonMainSmall(
                    modifier = Modifier
                        .defaultMinSize(minWidth = 81.dp)
                        .padding(16.dp),
                    text = "Enabled",
                    onClick = {}
                )
                ButtonMainSmall(
                    modifier = Modifier
                        .defaultMinSize(minWidth = 81.dp)
                        .padding(16.dp),
                    isEnabled = false,
                    text = "Disabled",
                    onClick = {}
                )
                ButtonMainSmall(
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