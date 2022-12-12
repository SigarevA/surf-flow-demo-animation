package ru.surfstudio.android.demo.components.compose.overload.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun AnimatedBottomNavigation(
    selectedIndex: Int,
    countTab: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    heightHill : Dp = DefaultHeightHill.dp,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = BottomNavigationDefaults.Elevation,
    content: @Composable RowScope.() -> Unit
) {
    check(selectedIndex < countTab) {
        "selectedIndex must be less countTab"
    }
    check(selectedIndex >= 0 && countTab > 0) {
        "selectedIndex and countTab must be positive"
    }

    BoxWithConstraints(
        modifier = modifier
            .height(56.dp + heightHill)
    ) {
        val widthHill = remember(countTab) {
            maxWidth / countTab
        }

        val animOffset by animateDpAsState(
            targetValue = widthHill.times(selectedIndex),
            animationSpec = spring(Spring.DampingRatioMediumBouncy)
        )

        Canvas(
            Modifier
                .graphicsLayer {
                    translationX = animOffset.toPx()
                }
        ) {
            drawHill(
                widthHill.toPx(),
                backgroundColor,
                heightHill.toPx()
            )
        }

        CompositionLocalProvider(
            LocalContentColor provides contentColor
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(backgroundColor)
                    .heightIn(min = 56.dp, max = 56.dp)
                    .selectableGroup(),
                horizontalArrangement = Arrangement.SpaceBetween,
                content = content
            )
        }
    }
}

private fun DrawScope.drawHill(
    fullWidth : Float,
    backgroundColor : Color,
    heightHill: Float
) {
    val width = 2 * (76 / 41) * heightHill
    val centerX = fullWidth / 2f

    val ds = Path().apply {
        moveTo(centerX, 0f)
        relativeCubicTo(
            -(width / 2),
            0f,
            -width / 2,
            heightHill,
            -width,
            heightHill,
        )
        lineTo(0f, heightHill)
        lineTo(centerX, heightHill)

        moveTo(centerX, 0f)
        relativeCubicTo(
            width / 2,
            0f,
            width / 2,
            heightHill,
            width,
            heightHill,
        )
        lineTo(0f, heightHill)
        lineTo(centerX, heightHill)
    }

    drawPath(ds, backgroundColor)
}

private const val DefaultHeightHill = 24