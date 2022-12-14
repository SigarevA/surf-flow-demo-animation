package ru.surfstudio.android.demo.components.compose.overload.navigation

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun RowScope.AnimatedNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    iconResId: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    selectedContentColor: Color = LocalContentColor.current,
    unselectedContentColor: Color = selectedContentColor.copy(alpha = 0.4f)
) {
    val ripple = rememberRipple(bounded = false, color = Color.Red, radius = 100.dp)

    Box(
        modifier
            .selectable(
                selected = selected,
                onClick = onClick,
                enabled = enabled,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = ripple
            )
            .weight(1f),
        contentAlignment = Alignment.Center
    ) {
        BottomNavigationTransition(
            selectedContentColor,
            unselectedContentColor,
            selected
        ) { progress, offset ->
            BottomNavigationItemBaselineLayout(iconResId, progress, offset)
        }
    }
}

@Composable
private fun BottomNavigationTransition(
    activeColor: Color,
    inactiveColor: Color,
    selected: Boolean,
    content: @Composable (animationProgress: Float, animationOffset: Dp) -> Unit
) {
    val animationProgress by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = BottomNavigationAnimationSpec
    )

    val keyFrames = keyframes<Dp> {
        durationMillis = 300
        (-4).dp at 50
        20.dp at 250
    }

    val tween = tween<Dp>(
        BottomNavigationAnimationSpec.durationMillis,
        easing = BottomNavigationAnimationSpec.easing
    )

    val offsetTransition = updateTransition(if (selected) 18.dp else 0.dp, label = "")
    val offsetAnimationIn by offsetTransition.animateDp(
        transitionSpec = { keyFrames },
        label = ""
    ) { it }

    val offsetAnimationOut by offsetTransition.animateDp(
        transitionSpec = { tween },
        label = ""
    ) {
        it
    }

    val offsetAnimation by rememberUpdatedState(newValue = if (!selected) offsetAnimationOut else offsetAnimationIn)

    val scaleIcon by animateFloatAsState(
        targetValue = if (selected) 1.1f else 1f,
        animationSpec = keyframes {
            durationMillis = 300
            0.85f at 50
            1f at 150
            1.15f at 250
        }
    )

    val color = androidx.compose.ui.graphics.lerp(inactiveColor, activeColor, animationProgress)

    CompositionLocalProvider(
        LocalContentColor provides color.copy(alpha = 1f),
    ) {
        content(scaleIcon, offsetAnimation)
    }
}

private val BottomNavigationAnimationSpec = TweenSpec<Float>(
    durationMillis = 400,
    easing = FastOutSlowInEasing
)

@Composable
private fun BottomNavigationItemBaselineLayout(
    @DrawableRes iconResId: Int,
    scaleICon: Float,
    animationOffset: Dp,
) {
    Layout(
        {
            Box(
                Modifier
                    .layoutId("icon")
                    .offset(y = -animationOffset)
                    .scale(scaleICon)
            ) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }
    ) { measurables, constraints ->
        val iconPlaceable = measurables.first { it.layoutId == "icon" }.measure(constraints)

        val height = constraints.maxHeight
        val iconY = (height - iconPlaceable.height) / 2
        layout(iconPlaceable.width, height) {
            iconPlaceable.placeRelative(0, iconY)
        }
    }
}