package ru.surfstudio.android.demo.components.compose.overload.buttons


import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

@Composable
fun SubscribeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    borderWidth: Float = 4f,
    colorBackground: Color = Color.Red,
    colorContent: Color = Color.White,
) {
    val dp = LocalDensity.current.density

    val scaleBorderWidth = 4 * dp

    var isActive by remember { mutableStateOf(true) }
    var isSubscribe by remember { mutableStateOf(false) }

    var transitionUnSubscribeAnimationState by remember {
        mutableStateOf(false)
    }
    var transitionSubscribeAnimation by remember {
        mutableStateOf(false)
    }

    val progressAnimation = remember { Animatable(0f) }
    val widthDifferenceAnimation = remember { Animatable(0f) }
    val colorBorder = remember { Animatable(0f) }
    val backgroundContentAlpha = remember { Animatable(0f) }
    val fadeActiveBtnAnimation = remember { Animatable(1f) }

    val completeAnimationState = rememberCompleteAnimationState(
        transitionSubscribeAnimation,
        transitionUnSubscribeAnimationState
    )

    if (transitionSubscribeAnimation) {
        LaunchedEffect(Unit) {
            val fadeAnim = launch {
                fadeActiveBtnAnimation.animateTo(
                    0f,
                    animationSpec = TweenSpec(
                        durationMillis = 250
                    )
                )
            }
            val widthAnim = launch {
                widthDifferenceAnimation.animateTo(
                    1f,
                    animationSpec = TweenSpec(
                        durationMillis = AnimationDuration
                    )
                )
            }
            val colorAnim = launch {
                colorBorder.animateTo(
                    1f,
                    animationSpec = TweenSpec(
                        durationMillis = AnimationDuration
                    )
                )
            }
            joinAll(fadeAnim, widthAnim, colorAnim)
            progressAnimation.animateTo(
                360f,
                animationSpec = tween(
                    durationMillis = AnimationDuration,
                    easing = FastOutLinearInEasing,
                )
            )
            colorBorder.snapTo(0f)
            val expandWidthAnimJob = launch {
                widthDifferenceAnimation.animateTo(
                    0f,
                    animationSpec = tween(
                        durationMillis = AnimationDuration
                    )
                )
            }
            val alphaAnimJob = launch {
                backgroundContentAlpha.animateTo(
                    1f,
                    animationSpec = tween(
                        durationMillis = AnimationDuration
                    )
                )
            }
            joinAll(expandWidthAnimJob, alphaAnimJob)
            delay(700)
            isSubscribe = true
            transitionSubscribeAnimation = false
        }
    }

    if (transitionUnSubscribeAnimationState) {
        LaunchedEffect(Unit) {
            delay(1400)
            val alphaAnimJob = launch {
                backgroundContentAlpha.animateTo(
                    0f,
                    animationSpec = tween(
                        durationMillis = AnimationDuration
                    )
                )
            }
            val widthAnim = launch {
                widthDifferenceAnimation.animateTo(
                    1f,
                    animationSpec = TweenSpec(
                        durationMillis = AnimationDuration
                    )
                )
            }

            joinAll(widthAnim, alphaAnimJob)

            progressAnimation.animateTo(
                0f,
                animationSpec = tween(
                    durationMillis = AnimationDuration,
                    easing = LinearOutSlowInEasing,
                )
            )

            val widthAnimExpand = launch {
                widthDifferenceAnimation.animateTo(
                    0f,
                    animationSpec = tween(
                        durationMillis = AnimationDuration
                    )
                )
            }
            val fadeAnim = launch {
                fadeActiveBtnAnimation.animateTo(
                    1f,
                    animationSpec = TweenSpec(
                        durationMillis = 250,
                        delay = AnimationDuration - 250
                    )
                )
            }

            joinAll(widthAnimExpand, fadeAnim)

            isActive = true
            transitionUnSubscribeAnimationState = false
        }
    }

    Box(
        modifier = modifier
    ) {
        val mainContent = @Composable {
            Box(
                modifier = Modifier.alpha(fadeActiveBtnAnimation.value)
            ) {
                ActiveSubscribeButtonComponent(
                    isActive = isActive,
                    colorBackground = Color.Green,
                    onClick = {
                        if (isActive) {
                            onClick()
                            isActive = false
                            transitionSubscribeAnimation = true
                        }
                    },
                    text = {
                        Text(
                            text = text,
                            letterSpacing = 2.sp,
                            color = colorBackground
                        )
                    }
                )
            }
        }

        val dependent: @Composable (DpSize) -> Unit = @Composable { size ->
            val dp = LocalDensity.current
            BackgroundSubscribeButton(
                scaleBorderWidth = scaleBorderWidth,
                scaleSize = Size(
                    size.width.value * dp.density,
                    size.height.value * dp.density
                ),
                widthAnimation = widthDifferenceAnimation.value,
                colorBorder = colorBorder.value,
                colorBackground = colorBackground,
                colorContent = colorContent,
                backgroundContentAlpha = backgroundContentAlpha.value,
                progressAnimation = progressAnimation.value,
                transitionSubscribeAnimation = transitionSubscribeAnimation,
                transitionUnSubscribeAnimationState = transitionUnSubscribeAnimationState,
                completeAnimationState = completeAnimationState
            )

            if (isSubscribe) {
                CompleteSubscribeButtonComponent(
                    colorBackground = colorBackground,
                    colorContent = colorContent,
                    dpSize = size,
                    onClick = {
                        transitionUnSubscribeAnimationState = true
                        isSubscribe = false
                    }
                )
            }
        }

        SubcomposeLayout { constraints ->
            val mainPlaceables = subcompose("main", mainContent).map {
                it.measure(constraints)
            }
            Log.d(TAG, "count : ${mainPlaceables.size}")
            Log.d(TAG, "")

            val firstChild = mainPlaceables[0]

            Log.d(TAG, "Width : ${firstChild.width.toDp()} , height : ${firstChild.height.toDp()}")

            layout(constraints.maxWidth, constraints.maxHeight) {
                mainPlaceables.forEach {
                    it.placeRelative(0, 0)
                }
                subcompose(
                    "depend"
                ) {
                    dependent(
                        DpSize(
                            firstChild.width.toDp(),
                            firstChild.height.toDp()
                        )
                    )
                }.map { it.measure(constraints) }
                    .forEach {
                        it.placeRelative(0, 0)
                    }
            }
        }
    }
}

@Composable
private fun BackgroundSubscribeButton(
    scaleBorderWidth: Float,
    scaleSize: Size,
    widthAnimation: Float,
    colorBorder: Float,
    colorBackground: Color,
    colorContent: Color,
    backgroundContentAlpha: Float,
    progressAnimation: Float,
    transitionSubscribeAnimation: Boolean,
    transitionUnSubscribeAnimationState: Boolean,
    completeAnimationState: State<CompleteSubscriptionAnimationState>
) {
    val widthDeference = remember {
        scaleSize.width - scaleSize.height
    }

    Canvas(modifier = Modifier) {
        val backgroundContent = Path().apply {
            addRoundRect(
                RoundRect(
                    Rect(
                        Offset(
                            scaleBorderWidth / 2 + (widthDeference * widthAnimation) / 2f,
                            scaleBorderWidth / 2
                        ),
                        Size(
                            scaleSize.width - scaleBorderWidth - (widthDeference * widthAnimation),
                            scaleSize.height - scaleBorderWidth
                        )
                    ),
                    cornerRadius = CornerRadius(scaleSize.height - scaleBorderWidth)
                )
            )
        }

        drawBorder(
            scaleSize,
            widthDeference * widthAnimation,
            scaleBorderWidth,
            lerp(Color.Red, Color.Gray, colorBorder)
        )

        if (backgroundContentAlpha > 0) {
            drawPath(
                backgroundContent,
                lerp(
                    colorBackground.copy(alpha = if (transitionUnSubscribeAnimationState) 0f else .6f),
                    colorBackground,
                    backgroundContentAlpha
                )
            )
        } else {
            drawArc(
                if (transitionSubscribeAnimation) Color.Red else Color.Gray,
                -90f,
                progressAnimation,
                false,
                topLeft = Offset((widthDeference * widthAnimation) / 2f + 2.dp.toPx(), 2.dp.toPx()),
                size = Size(
                    scaleSize.height - scaleBorderWidth,
                    scaleSize.height - scaleBorderWidth
                ),
                style = Stroke(width = scaleBorderWidth, cap = Round)
            )
        }
    }
    if (backgroundContentAlpha > 0) {
        CompleteSubscriptionComponent(
            completeAnimationState.value,
            colorContent = colorContent,
            offset = Offset(
                scaleSize.width / 2,
                scaleSize.height / 2,
            )
        )
    }
}

@Composable
private fun CompleteSubscribeButtonComponent(
    colorBackground: Color,
    colorContent: Color,
    dpSize: DpSize,
    onClick: () -> Unit,
) {
    Button(
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorBackground
        ),
        elevation = ButtonDefaults.elevation(
            0.dp, 0.dp, 0.dp, 0.dp, 0.dp,
        ),
        shape = RoundedCornerShape(50),
        contentPadding = PaddingValues(),
        modifier = Modifier
            .size(dpSize)
    ) {
        CompleteSubscriptionComponent(
            colorContent = colorContent,
            animationState = CompleteSubscriptionAnimationState.No,
        )
    }
}

@Composable
private fun ActiveSubscribeButtonComponent(
    isActive: Boolean,
    colorBackground: Color,
    borderWidth: Dp = Dp(4f),
    onClick: () -> Unit,
    text: @Composable () -> Unit,
) {
    TextButton(
        onClick = {
            onClick()
        },
        contentPadding = PaddingValues(
            vertical = 8.dp,
            horizontal = 32.dp
        ),
        border = if (isActive) BorderStroke(
            borderWidth, colorBackground
        ) else null,
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .height(40.dp)
    ) {
        text()
    }
}

private const val AnimationDuration = 1250

private fun DrawScope.drawBorder(
    scaleSize: Size,
    widthAnimation: Float,
    borderWidth: Float,
    borderColor: Color
) {
    Path.combine(
        PathOperation.Difference,
        Path().apply {
            addRoundRect(
                RoundRect(
                    Rect(
                        Offset(0f + widthAnimation / 2f, 0f),
                        Size(scaleSize.width - widthAnimation, scaleSize.height)
                    ),
                    cornerRadius = CornerRadius(scaleSize.height)
                )
            )
        },
        Path().apply {
            addRoundRect(
                RoundRect(
                    Rect(
                        Offset(borderWidth + widthAnimation / 2f, borderWidth),
                        Size(
                            scaleSize.width - 2 * borderWidth - widthAnimation,
                            scaleSize.height - 2 * borderWidth
                        )
                    ),
                    cornerRadius = CornerRadius(scaleSize.height - 2 * borderWidth)
                )
            )
        }
    ).let { drawPath(it, borderColor) }
}

@Composable
private fun CompleteSubscriptionComponent(
    animationState: CompleteSubscriptionAnimationState,
    colorContent: Color,
    offset: Offset = Offset.Zero,
) {
    Log.d("CompleteSubscriptionComponent", "animationState : ${animationState}")

    fun CompleteSubscriptionAnimationState.isExpand() =
        this == CompleteSubscriptionAnimationState.Expand

    val dp = LocalDensity.current.density
    val leftLineLengthAnimation = remember {
        Animatable(
            if (animationState.isExpand())
                0f
            else 6f * dp
        )
    }

    val rightLineLengthAnimation =
        remember { Animatable(if (animationState.isExpand()) 0f else 16f * dp) }

    val leftStartLineLengthAnimation = remember { Animatable(0f) }
    val rightStartLineLengthAnimation = remember { Animatable(0f) }

    when (animationState) {
        CompleteSubscriptionAnimationState.Expand -> {
            LaunchedEffect(Unit) {
                launch {
                    leftLineLengthAnimation.animateTo(
                        6f * dp,
                        animationSpec = TweenSpec(
                            durationMillis = 750
                        )
                    )
                    rightLineLengthAnimation.animateTo(
                        16f * dp,
                        animationSpec = TweenSpec(
                            durationMillis = 900
                        )
                    )
                }
            }
        }
        CompleteSubscriptionAnimationState.Hidden -> {
            LaunchedEffect(Unit) {
                launch {
                    leftStartLineLengthAnimation.animateTo(
                        6f * dp,
                        animationSpec = TweenSpec(
                            durationMillis = 750
                        )
                    )
                    rightStartLineLengthAnimation.animateTo(
                        16f * dp,
                        animationSpec = TweenSpec(
                            durationMillis = 900
                        )
                    )
                }
            }
        }
        else -> {}
    }

    Canvas(modifier = Modifier
        .graphicsLayer {
            translationY =
                offset.y - 16.dp.toPx() / 2 - 8.dp.toPx()// 90.dp.toPx() + 44.dp.toPx() / 2 - 16.dp.toPx() / 2 - 8.dp.toPx()
            translationX = offset.x - 16 * dp // 78.dp.toPx() - 16f * dp
        }
    ) {
        if (leftStartLineLengthAnimation.value != leftLineLengthAnimation.value)
            drawLine(
                color = colorContent,
                start = Offset(
                    6.dp.toPx() + leftStartLineLengthAnimation.value,
                    18.dp.toPx() + leftStartLineLengthAnimation.value,
                ),
                end = Offset(
                    6.dp.toPx() + leftLineLengthAnimation.value,
                    18.dp.toPx() + leftLineLengthAnimation.value,
                ),
                strokeWidth = 4.dp.toPx(),
                cap = Round,
            )
        if (rightStartLineLengthAnimation.value != rightLineLengthAnimation.value) {
            drawLine(
                color = colorContent,
                start = Offset(
                    12.dp.toPx() + rightStartLineLengthAnimation.value,
                    24.dp.toPx() - rightStartLineLengthAnimation.value,
                ),
                end = Offset(
                    12.dp.toPx() + rightLineLengthAnimation.value,
                    24.dp.toPx() - rightLineLengthAnimation.value,
                ),
                strokeWidth = 4.dp.toPx(),
                cap = Round,
            )
        }
    }
}

@Composable
private fun rememberCompleteAnimationState(
    transitionSubscribeAnimation: Boolean,
    transitionUnSubscribeAnimationState: Boolean
): State<CompleteSubscriptionAnimationState> {
    return remember(
        transitionSubscribeAnimation,
        transitionUnSubscribeAnimationState
    ) {
        derivedStateOf {
            if (transitionUnSubscribeAnimationState)
                CompleteSubscriptionAnimationState.Hidden
            else if (transitionSubscribeAnimation)
                CompleteSubscriptionAnimationState.Expand
            else
                CompleteSubscriptionAnimationState.No
        }
    }
}

enum class CompleteSubscriptionAnimationState {
    No, Expand, Hidden;
}

private const val TAG = "SubscribeButton"