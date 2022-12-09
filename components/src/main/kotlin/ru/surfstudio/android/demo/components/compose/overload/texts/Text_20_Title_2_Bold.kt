/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.components.compose.overload.texts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import ru.surfstudio.android.demo.components.compose.base.texts.AppText
import ru.surfstudio.android.demo.components.compose.theme.AppTheme

@Composable
fun Text_Title_2_Bold_20(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    color: Color = Color.Unspecified
) = AppText(
    maxLines = maxLines,
    textAlign = textAlign,
    modifier = modifier,
    style = AppTheme.typography.Title_2_Bold_20,
    text = text,
    color = color
)