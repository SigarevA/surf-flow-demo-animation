package ru.surfstudio.android.demo.components.compose.overload.navigation

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun NavigationBarWithHill(
    modifier: Modifier = Modifier
) {
    val currentState = remember { mutableStateOf(0.dp) }
    val updateTransition = updateTransition(currentState, label = "")
    val marginSize by updateTransition.animateDp(
        transitionSpec = { tween(durationMillis = 2000) },
        label = ""
    ) {
        it.value
    }

//    val transition = updateTransition(MutableTransitionState(0f), label = "")
//    transition.animateDp(label = "") { it.dp }
//    val infiniteTransition = rememberInfiniteTransition()
//    val color by infiniteTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = 100.dp.toPx(),
//        animationSpec = repeatable(
//            1,
//            TweenSpec()
//        )
//        animationSpec = infiniteRepeatable(
//            animation = tween(1000, easing = LinearEasing),
//            repeatMode = RepeatMode.Reverse
//        )
//    )


    Box(modifier = Modifier.fillMaxSize()) {

        Canvas(Modifier) {
            val path = Path().apply {
                cubicTo(75.dp.toPx(), 125.dp.toPx(), 150.dp.toPx(), 75.dp.toPx(), 200.dp.toPx(), 0f)
//                quadraticBezierTo(100f, 100f, 200f, 0f)
            }
            drawPath(path, Color.Red)
        }

        Text("dasd", modifier.padding(top = 400.dp, start = marginSize))
    }


    SideEffect {
        currentState.value = 100.dp
    }
}

@Composable
fun RowScope.AnimatedNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    iconResId: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    selectedContentColor: Color = LocalContentColor.current,
    unselectedContentColor: Color = selectedContentColor.copy(alpha = ContentAlpha.medium)
) {
    val ripple = rememberRipple(bounded = false, color = selectedContentColor)

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
//            BottomNavigationItemBaselineLayout(
//                icon = icon,
//                label = styledLabel,
//                iconPositionAnimationProgress = progress
//            )
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
        12.dp at 250
    }

    val tween = tween<Dp>(
        BottomNavigationAnimationSpec.durationMillis,
        easing = BottomNavigationAnimationSpec.easing
    )

    val keyStep by rememberUpdatedState(if (selected) 8.dp else -2.dp)
    val keyStep1 by rememberUpdatedState<FiniteAnimationSpec<Dp>>(
        if (selected)
            tween(
                BottomNavigationAnimationSpec.durationMillis,
                easing = BottomNavigationAnimationSpec.easing
            )
        else keyframes {
            durationMillis = 300
            -2.dp at 50
            12.dp at 250
        }
    )

    val currentState = remember { mutableStateOf(0.dp) }
    val offsetTransition = updateTransition(if (selected) 10.dp else 0.dp, label = "")
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
        LocalContentAlpha provides color.alpha,
    ) {
        content(scaleIcon, offsetAnimation)
    }
}

private val BottomNavigationAnimationSpec = TweenSpec<Float>(
    durationMillis = 300,
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
            ) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .scale(scaleICon)
                )
            }
        }
    ) { measurables, constraints ->
        val iconPlaceable = measurables.first { it.layoutId == "icon" }.measure(constraints)

        placeIcon(iconPlaceable, constraints)
    }
}

/**
 * Places the provided [iconPlaceable] in the vertical center of the provided [constraints]
 */
private fun MeasureScope.placeIcon(
    iconPlaceable: Placeable,
    constraints: Constraints
): MeasureResult {
    val height = constraints.maxHeight
    val iconY = (height - iconPlaceable.height) / 2
    return layout(iconPlaceable.width, height) {
        iconPlaceable.placeRelative(0, iconY)
    }
}

@Composable
fun AnimatedBottomNavigation2(
    selectedIndex: Int,
    countTab: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
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
            .height(88.dp)
    ) {
        val widthHill = remember(countTab) {
            maxWidth / countTab
        }

        val animOffset by animateDpAsState(
            targetValue = widthHill.times(selectedIndex),
            animationSpec = spring(Spring.DampingRatioMediumBouncy)
        )

        Surface(
            color = backgroundColor,
            contentColor = contentColor,
            elevation = elevation,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectableGroup(),
                horizontalArrangement = Arrangement.SpaceBetween,
                content = content
            )
        }
        Canvas(Modifier
//            .background(Color.Green)
            .graphicsLayer {
                translationX = animOffset.toPx()
//            translationY = 24.dp.toPx()
            }
//            .background(Color.Green)
        ) {

            val height = HeightHill.dp.toPx()
            val width = widthHill.toPx()

            val trianglePath = Path().apply {
                moveTo(
                    width / 2 + (width / 4) - 2f, 0.33f * height - 1f
                )
                relativeLineTo(0f, 0.66f * height + 2f)
                relativeLineTo(width * 0.25f, 0f)
                relativeLineTo(0f, 0f)
            }

            val trianglePathRight = Path().apply {
                moveTo(
                    width / 2 - (width / 4) + 4f, 0.33f * height - 1f
                )
                relativeLineTo(0f, 0.66f * height + 2f)
                relativeLineTo(-width * 0.25f, 0f)
                relativeLineTo(0f, 0f)
            }

            val trianglePathSub = Path().apply {
                moveTo(
                    width / 2 + (width / 4) - 2f, 0.33f * height - 1f
                )
                relativeCubicTo(
                    width / 8 - 2f,
                    0.3f * height - 2f,
                    width / 12,
                    0.66f * height - 2f,
                    width / 4,
                    0.66f * height + 2f,
                )
            }

            val path = Path().apply {
                moveTo(
                    0f, HeightHill.dp.toPx()
                )
                cubicTo(
                    widthHill.div(3).toPx(),
                    0.dp.toPx(),
                    widthHill.times(2).div(3).toPx(),
                    0.dp.toPx(),
                    widthHill.toPx(),
                    HeightHill.dp.toPx()
                )
//                quadraticBezierTo(100f, 100f, 200f, 0f)
            }
            val path2 = Path().apply {
                moveTo(
                    widthHill.toPx() - 5.dp.toPx(), HeightHill.dp.toPx() - 4.dp.toPx()
                )
                relativeQuadraticBezierTo(
                    10.dp.toPx(),
                    4.dp.toPx(),
                    7.dp.toPx(),
                    7.dp.toPx()
                )
//                quadraticBezierTo(
//                    widthHill.toPx(), height - 5.dp.toPx(), widthHill.toPx() + 10.dp.toPx(), height
//                )
//                quadraticBezierTo(100f, 100f, 200f, 0f)
            }
            val path3 = Path().apply {
                moveTo(
                    widthHill.toPx() - 5.dp.toPx(), HeightHill.dp.toPx() - 3.dp.toPx()
                )
                relativeQuadraticBezierTo(
                    6.dp.toPx(),
                    4.dp.toPx(),
                    9.dp.toPx(),
                    7.dp.toPx()
                )
            }

            val path_roof = Path().apply {
                moveTo(
                    width / 2 - (width / 4), 0.33f * height + 2f
                )
                relativeCubicTo(
                    width / 6,
                    -0.33f * height - 4f,
                    width / 3,
                    -0.33f * height - 4f,
                    width / 2 + 2f,
                    0f
                )
//                quadraticBezierTo(100f, 100f, 200f, 0f)
            }

            val resultRounded = Path.combine(PathOperation.Union, path2, path3)
//            drawPath(trianglePath, Color.Red)
            drawRect(
                backgroundColor,
                Offset(width / 2 - (width / 4), 0.33f * height + 1f),
                Size(width / 2, 0.66f * height + 1.dp.toPx())
            )
            drawPath(path_roof, backgroundColor)
            drawPath(trianglePathRight, backgroundColor)
            drawPath(
                Path.combine(PathOperation.Difference, trianglePath, trianglePathSub),
                backgroundColor
            )
//            drawPath(Path.combine(PathOperation.Difference, path, path3), backgroundColor)
//            drawPath(path3, Color.Red, blendMode = BlendMode.DstIn)
        }
    }
}


@Composable
fun AnimatedBottomNavigation(
    selectedIndex: Int,
    countTab: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
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
            .height(88.dp)
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
//            .background(Color.Green)
                .matchParentSize()
                .clipToBounds()
                .graphicsLayer {
                    translationX = animOffset.toPx()
//            translationY = 24.dp.toPx()
                }
//            .background(Color.Green)
        ) {

            val height = HeightHill.dp.toPx()
            val width = widthHill.toPx()


            val path = Path().apply {
                moveTo(
                    0f, HeightHill.dp.toPx()
                )
                cubicTo(
                    widthHill.div(3).toPx(),
                    0.dp.toPx(),
                    widthHill.times(2).div(3).toPx(),
                    0.dp.toPx(),
                    widthHill.toPx(),
                    HeightHill.dp.toPx()
                )
//                quadraticBezierTo(100f, 100f, 200f, 0f)
            }
            val path2 = Path().apply {
                moveTo(
                    widthHill.toPx() - 5.dp.toPx(), HeightHill.dp.toPx() - 4.dp.toPx()
                )
                relativeQuadraticBezierTo(
                    10.dp.toPx(),
                    4.dp.toPx(),
                    7.dp.toPx(),
                    7.dp.toPx()
                )
//                quadraticBezierTo(
//                    widthHill.toPx(), height - 5.dp.toPx(), widthHill.toPx() + 10.dp.toPx(), height
//                )
//                quadraticBezierTo(100f, 100f, 200f, 0f)
            }
            val path3 = Path().apply {
                moveTo(
                    widthHill.toPx() - 5.dp.toPx(), HeightHill.dp.toPx() - 3.dp.toPx()
                )
                relativeQuadraticBezierTo(
                    6.dp.toPx(),
                    4.dp.toPx(),
                    9.dp.toPx(),
                    7.dp.toPx()
                )
            }

            val path_roof = Path().apply {
                moveTo(
                    width / 2 - (width / 4), 0.33f * height + 2f
                )
                relativeCubicTo(
                    width / 6,
                    -0.33f * height - 4f,
                    width / 3,
                    -0.33f * height - 4f,
                    width / 2 + 2f,
                    0f
                )
//                quadraticBezierTo(100f, 100f, 200f, 0f)
            }

            val resultRounded = Path.combine(PathOperation.Union, path2, path3)
//            drawPath(trianglePath, Color.Red)
            drawCircle(
                color = backgroundColor,
                center = Offset(width / 2f, height + 6.dp.toPx()),
                radius = 36.dp.toPx()
            )
//            drawOval(
//                color = Color.Green,
//                size = Size(68.dp.toPx(), 72.dp.toPx()),
//                topLeft = Offset(width / 2f + 36.dp.toPx() + 20.dp.toPx() - (68.dp.toPx() / 2), - 72.dp.toPx() + height)
//            )
            val pa = Path().apply {
                addOval(
                    Rect(
                        size = Size(68.dp.toPx(), 72.dp.toPx()),
                        offset = Offset(
                            width / 2f + 36.dp.toPx() + 20.dp.toPx() - (68.dp.toPx() / 2),
                            -72.dp.toPx() + height
                        )
                    )
                )
                addOval(
                    Rect(
                        size = Size(68.dp.toPx(), 72.dp.toPx()),
                        offset = Offset(
                            width / 2f - 36.dp.toPx() - 20.dp.toPx() - (68.dp.toPx() / 2),
                            -72.dp.toPx() + height
                        )
                    )
                )
            }
            val pa2 = Path().apply {
                addRect(
                    Rect(
                        size = Size(2f * 20.dp.toPx() + 2 * 36.dp.toPx(), 0.4f * 36.dp.toPx()),
                        offset = Offset(
                            width / 2f - 20.dp.toPx() - 36.dp.toPx(),
                            height - 0.4f * 36.dp.toPx()
                        )
                    )
                )
            }
            drawPath(
                color = backgroundColor,
                path = Path.combine(PathOperation.Difference, pa2, pa),
            )
//            drawRect(
//                backgroundColor,
//                Offset(width / 2 - (width / 4), 0.33f * height + 1f),
//                Size(width / 2, 0.66f * height + 1.dp.toPx())
//            )
//            drawPath(Path.combine(PathOperation.Difference, path, path3), backgroundColor)
//            drawPath(path3, Color.Red, blendMode = BlendMode.DstIn)
        }

        Surface(
            color = backgroundColor,
            contentColor = contentColor,
            elevation = elevation,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectableGroup(),
                horizontalArrangement = Arrangement.SpaceBetween,
                content = content
            )
        }
    }
}

private const val HeightHill = 32


@Preview(
    showBackground = true
)
@Composable
fun PreviewActivity() {
    var selectedTab by remember { mutableStateOf(Tabs.Smile) }
    Box {
        Text("Where?")
//            SurfMviDemoTheme {
        NavigationBarWithHill()

        AnimatedBottomNavigation(
            selectedTab.ordinal,
            Tabs.values().size,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Tabs.values().forEach { tab ->
                AnimatedNavigationBarItem(
                    selected = selectedTab == tab,
                    onClick = { selectedTab = tab },
                    iconResId = tab.iconResId,
                )
            }
        }
    }
}

enum class Tabs(val iconResId: Int) {
    Smile(ru.surfstudio.android.demo.resources.R.drawable.ic_smile),
    Star(ru.surfstudio.android.demo.resources.R.drawable.ic_star),
    Graph(ru.surfstudio.android.demo.resources.R.drawable.ic_graph),
    Menu(ru.surfstudio.android.demo.resources.R.drawable.ic_menu),
}