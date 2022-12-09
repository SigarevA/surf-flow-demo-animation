/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.components.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import ru.surfstudio.android.demo.components.compose.theme.custom.AppColors
import ru.surfstudio.android.demo.components.compose.theme.custom.AppShapes
import ru.surfstudio.android.demo.components.compose.theme.custom.AppTypography

private val DarkColorPalette = darkColors(
    primary = Turquoise,
    primaryVariant = TurquoiseLight,
    error = RedError,
)

private val LightColorPalette = lightColors(
    primary = Turquoise,
    primaryVariant = TurquoiseLight,
    error = RedError
)

private val AppLightColorPalette = AppColors(
    Turquoise = Turquoise,
    Turquoise_Light = TurquoiseLight,

    Active_Turquoise = ActiveTurquoise,
    Active_Turquoise_Light = ActiveTurquoiseLight,
    Active_Red = ActiveRed,

    Red_Black = RedBlack,
    Red_Black_2 = RedBlack2,
    Red_Black_3 = RedBlack3,
    Red_Dark = RedDark,

    Red_2 = Red2,
    Red = Red,
    Red_Error = RedError,
    Red_Light = RedLight,

    Orange = Orange,
    Yellow = Yellow,
    Green = Green,
    Green_Light = GreenLight,
    Green_Malachite = GreenMalachite,

    Steel_Gray_500 = SteelGray500,
    Steel_Gray_400 = SteelGray400,
    Steel_Gray_300 = SteelGray300,
    Steel_Gray_200 = SteelGray200,
    Steel_Gray_150 = SteelGray150,

    Gray_100 = Gray100,
    Gray_050 = Gray050,
    Gray_040 = Gray040,
)

private val AppDarkColorPalette = AppColors(
    Turquoise = Turquoise,
    Turquoise_Light = TurquoiseLight,

    Active_Turquoise = ActiveTurquoise,
    Active_Turquoise_Light = ActiveTurquoiseLight,
    Active_Red = ActiveRed,

    Red_Black = RedBlack,
    Red_Black_2 = RedBlack2,
    Red_Black_3 = RedBlack3,
    Red_Dark = RedDark,

    Red_2 = Red2,
    Red = Red,
    Red_Error = RedError,
    Red_Light = RedLight,

    Orange = Orange,
    Yellow = Yellow,
    Green = Green,
    Green_Light = GreenLight,
    Green_Malachite = GreenMalachite,

    Steel_Gray_500 = SteelGray500,
    Steel_Gray_400 = SteelGray400,
    Steel_Gray_300 = SteelGray300,
    Steel_Gray_200 = SteelGray200,
    Steel_Gray_150 = SteelGray150,

    Gray_100 = Gray100,
    Gray_050 = Gray050,
    Gray_040 = Gray040,
)

private val LocalAppColors = staticCompositionLocalOf<AppColors> {
    error("No AppColors provided")
}

private val LocalAppTypography = staticCompositionLocalOf<AppTypography> {
    error("No AppTypography provided")
}

private val LocalAppShapes = staticCompositionLocalOf<AppShapes> {
    error("No AppShapes provided")
}

object AppTheme {

    val colors: AppColors
        @Composable
        get() = LocalAppColors.current

    val typography: AppTypography
        @Composable
        get() = LocalAppTypography.current

    val shapes: AppShapes
        @Composable
        get() = LocalAppShapes.current
}

@Composable
fun SurfMviDemoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    val appColors = if (darkTheme) {
        AppDarkColorPalette
    } else {
        AppLightColorPalette
    }

    CompositionLocalProvider(
        LocalAppColors provides appColors,
        LocalAppTypography provides AppTypography,
        LocalAppShapes provides AppShapes
    ) {
        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}