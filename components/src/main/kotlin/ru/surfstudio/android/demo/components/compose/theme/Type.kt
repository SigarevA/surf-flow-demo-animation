/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.components.compose.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.surfstudio.android.demo.components.compose.theme.custom.AppTypography

internal val Typography = androidx.compose.material.Typography()

internal val AppTypography = AppTypography(
    Title_1_Bold_32 = TextStyle(
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 38.sp,
        letterSpacing = 0.5.sp
    ),

    Title_1_Medium_24 = TextStyle(
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.15.sp
    ),
    Title_1_Bold_24 = TextStyle(
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 28.sp,
    ),
    Title_1_Black_24 = TextStyle(
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Black,
        fontSize = 24.sp,
        lineHeight = 28.sp,
    ),

    Title_2_Regular_20 = TextStyle(
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.15.sp
    ),
    Title_2_Medium_20 = TextStyle(
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.15.sp
    ),
    Title_2_Bold_20 = TextStyle(
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.25.sp
    ),
    Title_2_Black_20 = TextStyle(
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Black,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.25.sp
    ),

    Title_3_Regular_18 = TextStyle(
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.15.sp
    ),
    Title_3_Medium_18 = TextStyle(
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.15.sp
    ),
    Title_3_Bold_18 = TextStyle(
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.15.sp
    ),

    Headline_Regular_16 = TextStyle(
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.15.sp
    ),
    Headline_Medium_16 = TextStyle(
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.15.sp
    ),
    Headline_Bold_16 = TextStyle(
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.15.sp
    ),

    Text_Regular_14 = TextStyle(
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    Text_Medium_14 = TextStyle(
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    Text_Caps_Medium_14 = TextStyle(
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.75.sp
    ),
    Text_Bold_14 = TextStyle(
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),

    Caption_Regular_12 = TextStyle(
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    Caption_Medium_12 = TextStyle(
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    Caption_Caps_Medium_12 = TextStyle(
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    Caption_Bold_12 = TextStyle(
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.4.sp
    ),

    Caption_2_Regular_10 = TextStyle(
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.5.sp
    ),
    Caption_2_Medium_10 = TextStyle(
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.5.sp
    ),
    Caption_2_Caps_Medium_10 = TextStyle(
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.5.sp
    )
)