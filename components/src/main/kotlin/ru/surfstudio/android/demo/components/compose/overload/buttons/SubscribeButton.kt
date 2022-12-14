package ru.surfstudio.android.demo.components.compose.overload.buttons


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
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
    dpSize: DpSize = DpSize(Dp(160f), Dp(44f)),
    borderWidth : Float = 4f,
    colorBackground: Color = Color.Red,
    colorContent: Color = Color.White,
) {
    val dp = LocalDensity.current.density

    val scaleSize = Size(dpSize.width.value * dp, dpSize.height.value * dp)
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
    val widthAnimation = remember { Animatable(0f) }
    val colorBorder = remember { Animatable(0f) }
    val backgroundContentAlpha = remember { Animatable(0f) }

    val completeAnimationState = rememberCompleteAnimationState(
        transitionSubscribeAnimation,
        transitionUnSubscribeAnimationState
    )

    if (transitionSubscribeAnimation) {
        LaunchedEffect(Unit) {
            val widthAnim = launch {
                widthAnimation.animateTo(
                    scaleSize.width - scaleSize.height,
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
            joinAll(widthAnim, colorAnim)
            progressAnimation.animateTo(
                360f,
                animationSpec = tween(
                    durationMillis = AnimationDuration,
                    easing = FastOutLinearInEasing,
                )
            )
            colorBorder.snapTo(0f)
            val expandWidthAnimJob = launch {
                widthAnimation.animateTo(
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
                widthAnimation.animateTo(
                    scaleSize.width - scaleSize.height,
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

            widthAnimation.animateTo(
                0f,
                animationSpec = tween(
                    durationMillis = AnimationDuration
                )
            )

            isActive = true
            transitionUnSubscribeAnimationState = false
        }
    }

    Box(
        modifier = modifier
    ) {

        AnimatedVisibility(
            visible = isActive,
            modifier = Modifier
                .padding(top = 90.dp),
            enter = fadeIn(
                animationSpec = tween(durationMillis = 250)
            ),
            exit = fadeOut(
                animationSpec = tween(durationMillis = 250)
            )
        ) {
            TextButton(
                onClick = {
                    onClick()
                    isActive = false
                    transitionSubscribeAnimation = true
                },
                contentPadding = PaddingValues(
                    vertical = 8.dp,
                    horizontal = 32.dp
                ),
                border = if (isActive) BorderStroke(
                    4.dp, colorBackground
                ) else null,
                shape = RoundedCornerShape(44.dp),
                modifier = Modifier
                    .size(dpSize)
            ) {
                Text(
                    text = text,
                    letterSpacing = 2.sp,
                    color = colorBackground
                )
            }
        }

        Box {
            Canvas(modifier = Modifier.graphicsLayer {
                translationY = 90.dp.toPx()
            }) {
                val backgroundContent = Path().apply {
                    addRoundRect(
                        RoundRect(
                            Rect(
                                Offset(
                                    scaleBorderWidth / 2 + widthAnimation.value / 2f,
                                    scaleBorderWidth / 2
                                ),
                                Size(
                                    scaleSize.width - scaleBorderWidth - widthAnimation.value,
                                    scaleSize.height - scaleBorderWidth
                                )
                            ),
                            cornerRadius = CornerRadius(scaleSize.height - scaleBorderWidth)
                        )
                    )
                }

                drawBorder(
                    scaleSize,
                    widthAnimation.value,
                    scaleBorderWidth,
                    lerp(Color.Red, Color.Gray, colorBorder.value)
                )

                if (backgroundContentAlpha.value > 0) {
                    drawPath(
                        backgroundContent,
                        lerp(
                            colorBackground.copy(alpha = if (transitionUnSubscribeAnimationState) 0f else .6f),
                            colorBackground,
                            backgroundContentAlpha.value
                        )
                    )
                } else {
                    drawArc(
                        if (transitionSubscribeAnimation) Color.Red else Color.Gray,
                        -90f,
                        progressAnimation.value,
                        false,
                        topLeft = Offset(widthAnimation.value / 2f + 2.dp.toPx(), 2.dp.toPx()),
                        size = Size(scaleSize.height - scaleBorderWidth, scaleSize.height - scaleBorderWidth),
                        style = Stroke(width = scaleBorderWidth, cap = Round)
                    )
                }
            }
            if (backgroundContentAlpha.value > 0) {
                CompleteSubscriptionComponent(
                    completeAnimationState.value,
                    colorBackground = colorBackground,
                    colorContent = colorContent,
                    offset = Offset(
                        scaleSize.width / 2,
                        90 * dp + scaleSize.height / 2,
                    )
                )
            }
        }

        if (isSubscribe) {
            Button(
                onClick = {
                    transitionUnSubscribeAnimationState = true
                    isSubscribe = false
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorBackground
                ),
                elevation = ButtonDefaults.elevation(
                    0.dp, 0.dp, 0.dp, 0.dp, 0.dp,
                ),
                shape = RoundedCornerShape(dpSize.height),
                contentPadding = PaddingValues(),
                modifier = Modifier
                    .padding(top = 89.7.dp)
                    .size(dpSize)
            ) {
                CompleteSubscriptionComponent(
                    colorBackground = colorBackground,
                    colorContent = colorContent,
                    animationState = CompleteSubscriptionAnimationState.No,
                    offset = Offset(0f, -1f),
                )
            }
        }
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
    colorBackground: Color,
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
            pathEffect = PathEffect.cornerPathEffect(4.dp.toPx()),
            cap = Round,
        )
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

@Composable
private fun rememberCompleteAnimationState(
    transitionSubscribeAnimation : Boolean,
    transitionUnSubscribeAnimationState : Boolean
) : State<CompleteSubscriptionAnimationState> {
    return remember(
        transitionSubscribeAnimation,
        transitionUnSubscribeAnimationState
    ) {
        derivedStateOf {
            if (transitionUnSubscribeAnimationState)
                CompleteSubscriptionAnimationState.Hidden
            else
                CompleteSubscriptionAnimationState.Expand
        }
    }
}
enum class CompleteSubscriptionAnimationState {
    No, Expand, Hidden;
}

enum class SubscribeState {
    Active, Progress, Subscribed, Intermediate
}