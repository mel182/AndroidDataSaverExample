@file:OptIn(ExperimentalComposeUiApi::class)

package com.jetpackcompose.circularindicatordraggable.feature.ui

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jetpackcompose.circularindicatordraggable.feature.data.StartAngle
import com.jetpackcompose.circularindicatordraggable.feature.data.BLANK_STRING
import com.jetpackcompose.circularindicatordraggable.feature.data.CircularProgressColorScheme
import com.jetpackcompose.circularindicatordraggable.feature.data.CircularProgressTextStyle
import com.jetpackcompose.circularindicatordraggable.feature.data.DEFAULT_DOUBLE
import com.jetpackcompose.circularindicatordraggable.feature.domain.TouchAngleCalculationDelegate
import com.jetpackcompose.circularindicatordraggable.feature.domain.asTypeFace
import com.jetpackcompose.circularindicatordraggable.feature.domain.drawOuterIndicatorLines
import com.jetpackcompose.circularindicatordraggable.feature.domain.drawProgressBar
import com.jetpackcompose.circularindicatordraggable.feature.domain.drawProgressNumb
import com.jetpackcompose.circularindicatordraggable.feature.domain.drawProgressTrack
import com.jetpackcompose.circularindicatordraggable.feature.domain.drawStartPositionIndicatorLine
import com.jetpackcompose.circularindicatordraggable.feature.domain.round
import kotlin.math.abs
import kotlin.math.min

@Composable
fun ProgressBarCircular(
    modifier: Modifier = Modifier,
    colors: CircularProgressColorScheme = CircularProgressColorScheme(),
    textStyling: CircularProgressTextStyle = CircularProgressTextStyle(),
    startAngle: StartAngle,
    padding: Float = 50f,
    stroke: Float = 35f,
    cap: StrokeCap = StrokeCap.Round,
    minValue: Int = 0,
    maxValue: Int = 100,
    valueUnit: String = BLANK_STRING,
    showProgressNumb: Boolean,
    showOuterIndicatorLines: Boolean,
    onProgressChanged: (progress: Double) -> Unit
) {

    DrawCircularProgressBar(
        modifier = modifier,
        startAngle = startAngle,
        padding = padding,
        stroke = stroke,
        colors = colors,
        textStyling = textStyling,
        cap = cap,
        showProgressNumb = showProgressNumb,
        minValue = minValue,
        maxValue = maxValue,
        valueUnit = valueUnit,
        showOuterIndicatorLines = showOuterIndicatorLines,
        onProgressChanged = onProgressChanged
    )
}

@Composable
private fun DrawCircularProgressBar(
    modifier: Modifier = Modifier,
    padding: Float,
    stroke: Float,
    colors: CircularProgressColorScheme,
    textStyling: CircularProgressTextStyle,
    startAngle: StartAngle,
    showProgressNumb: Boolean,
    startPositionIndicatorStrokeWidth: Dp = 2.dp,
    showOuterIndicatorLines: Boolean = false,
    cap: StrokeCap = StrokeCap.Round,
    minValue: Int,
    maxValue: Int,
    valueUnit: String,
    onProgressChanged: (progress: Double) -> Unit
) {

    var width by remember { mutableIntStateOf(0) }
    var height by remember { mutableIntStateOf(0) }
    var radius by remember { mutableFloatStateOf(0f) }
    var center by remember { mutableStateOf(Offset.Zero) }

    var appliedAngle by remember {
        mutableDoubleStateOf(DEFAULT_DOUBLE)
    }
    var lastAngle by remember {
        mutableDoubleStateOf(DEFAULT_DOUBLE)
    }
    val circleCenter = remember {
        mutableStateOf(Offset.Zero)
    }
    var oldProgressValue by remember {
        mutableDoubleStateOf(DEFAULT_DOUBLE)
    }
    var displayedProgressValue by remember {
        mutableStateOf("$minValue")
    }

    val darkTheme = isSystemInDarkTheme()
    val colorScheme = colors.getColorScheme(isSystemInDarkTheme = darkTheme)
    val textStyle = textStyling.getTextStyle(isSystemInDarkTheme = darkTheme)
    val customTypeFace = textStyle.asTypeFace()

    Canvas(modifier = modifier
        .background(shape = CircleShape, color = Color.Transparent)
        .onGloballyPositioned {
            width = it.size.width
            height = it.size.height
            center = Offset(width / 2f, height / 2f)
            radius = min(width.toFloat(), height.toFloat()) / 2f - padding - stroke / 2f
        }
        .pointerInteropFilter {
            when (it.action) {

                MotionEvent.ACTION_DOWN -> Unit

                MotionEvent.ACTION_UP -> Unit

                MotionEvent.ACTION_MOVE -> {

                    val rawAppliedAngle by TouchAngleCalculationDelegate(
                        startAngle = startAngle,
                        center = center,
                        motionEvent = it
                    )
                    appliedAngle = rawAppliedAngle

                    val diff = abs(lastAngle - appliedAngle)
                    if (diff > 180) {
                        appliedAngle = if (appliedAngle < 180) {
                            360.0
                        } else {
                            0.0
                        }
                    }
                    val progress = appliedAngle / 360.0
                    oldProgressValue = (100 - 0) * progress

                    val percentageValue = (oldProgressValue / 100).toFloat()
                    val displayedValue = ((maxValue - minValue) * percentageValue)
                        .toDouble()
                        .round(2)
                    displayedProgressValue = "$displayedValue"
                    onProgressChanged(displayedValue)
                    lastAngle = appliedAngle

                }

                else -> return@pointerInteropFilter false
            }

            return@pointerInteropFilter true
        }) {

        drawProgressBar(
            color = colorScheme.progressBarColor,
            startAngle = startAngle,
            topLeft = center - Offset(radius, radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(
                width = stroke,
                cap = cap
            )
        )

        drawProgressTrack(
            color = colorScheme.trackColor,
            startAngle = startAngle,
            sweepAngle = abs(appliedAngle.toFloat()),
            topLeft = center - Offset(radius, radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(
                width = stroke,
                cap = cap
            )
        )

        if (showProgressNumb) {

            drawProgressNumb(
                colorScheme = colorScheme,
                startAngle = startAngle,
                center = center,
                appliedAngle = appliedAngle,
                stroke = stroke,
                radius = radius
            )
        } else {

            if (oldProgressValue == 0.0) {

                drawStartPositionIndicatorLine(
                    colorScheme = colorScheme,
                    startAngle = startAngle,
                    startPositionIndicatorStrokeWidth = startPositionIndicatorStrokeWidth,
                    center = center,
                    radius = radius,
                    appliedAngle = appliedAngle
                )
            }
        }

        if (showOuterIndicatorLines) {
            drawOuterIndicatorLines(
                stroke = stroke,
                circleCenter = circleCenter,
                colorScheme = colorScheme,
                displayText = "$displayedProgressValue $valueUnit",
                startAngle = startAngle,
                oldProgressValue = oldProgressValue,
                textStyle = textStyle,
                typeFace = customTypeFace,
                radius = radius
            )
        }
    }
}



