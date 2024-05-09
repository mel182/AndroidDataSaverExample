@file:OptIn(ExperimentalComposeUiApi::class)

package com.jetpackcompose.circularindicatordraggable.feature

import android.graphics.Paint
import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jetpackcompose.circularindicatordraggable.StartAngle
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

val ColorPrimary = Color(0xFF1c2026)
val LightGreen = Color(0xFF8dc387)
val ProgressBarBg = Color(0xFFFFE9DD)
val ProgressBarProgress = Color(0xFFE08868)
val ProgressBarTint = Color(0xFFE1BAAA)

@Composable
fun ProgressBarCircular(
    modifier: Modifier = Modifier,
    startAngle: StartAngle,
    padding: Float = 50f,
    stroke: Float = 35f,
    cap: StrokeCap = StrokeCap.Round,
    minValue: Int = 0,
    maxValue: Int = 100,
    valueUnit: String = "",
    showProgressNumb: Boolean,
    showOuterIndicatorLines: Boolean,
    onProgressChanged: (progress: Double) -> Unit
) {

    DrawCircularProgressBar(
        modifier = modifier,
        startAngle = startAngle,
        padding = padding,
        stroke = stroke,
        cap = cap,
        progressBarColor = ProgressBarBg,
        trackColor = ProgressBarProgress,
        numbOuterColor = ProgressBarTint,
        numbInnerColor = ColorPrimary,
        startPositionIndicatorColor = Color.Green,
        outerIndicatorLineColorUnSelected = Color.Blue.copy(alpha = 0.3f),
        showProgressNumb = showProgressNumb,
        minValue = minValue,
        maxValue = maxValue,
        valueUnit = valueUnit,
        showOuterIndicatorLines = showOuterIndicatorLines,
        outerIndicatorLineColorSelected = Color.Red,
        onProgressChanged = onProgressChanged
    )
}

@Composable
private fun DrawCircularProgressBar(
    modifier: Modifier = Modifier,
    padding: Float = 50f,
    stroke: Float = 35f,
    startAngle: StartAngle,
    progressBarColor: Color,
    trackColor: Color,
    showProgressNumb: Boolean,
    numbOuterColor: Color,
    numbInnerColor: Color,
    textStyle: androidx.compose.ui.text.TextStyle? = null,
    startPositionIndicatorColor: Color,
    startPositionIndicatorStrokeWidth: Dp = 2.dp,
    showOuterIndicatorLines: Boolean = false,
    outerIndicatorLineColorUnSelected: Color,
    outerIndicatorLineColorSelected: Color,
    cap: StrokeCap = StrokeCap.Round,
    minValue: Int,
    rotate: Float = 0.0f,
    maxValue: Int,
    valueUnit: String,
    initialAngle: Double = 0.0,
    onProgressChanged: (progress: Double) -> Unit
) {

    var width by remember { mutableIntStateOf(0) }
    var height by remember { mutableIntStateOf(0) }
    var radius by remember { mutableFloatStateOf(0f) }
    var center by remember { mutableStateOf(Offset.Zero) }

    var appliedAngle by remember {
        mutableDoubleStateOf(initialAngle)
    }
    var lastAngle by remember {
        mutableDoubleStateOf(0.0)
    }
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }
    var oldProgressValue by remember {
        mutableDoubleStateOf(initialAngle)
    }
    var displayedProgressValue by remember {
        mutableStateOf("0.0")
    }

    val customTypeFace = textStyle?.asTypeFace()

    Canvas(modifier = modifier
        .then(
            if (rotate != 0.0f) {
                Modifier.rotate(rotate)
            } else {
                Modifier
            }
        )
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
                    Log.i("TAG51", "")
                    Log.i("TAG34", "")
                    Log.i("TAG34", "|-------------------|")
                    Log.i("TAG34", "center X: ${center.x}")
                    Log.i("TAG34", "it X: ${it.x}")
                    Log.i("TAG34", "center Y: ${center.y}")
                    Log.i("TAG34", "it Y: ${it.y}")

                    val rawAppliedAngle by TouchAngleCalculationDelegate(
                        startAngle = startAngle,
                        center = center,
                        motionEvent = it
                    )
                    appliedAngle = rawAppliedAngle

                    Log.i("touch", "|----------------------|")
                    Log.i("touch", "Touch angle: $appliedAngle")
                    Log.i("touch", "|----------------------|")

                    Log.i("touch", "")
                    Log.i("touch", "")
                    Log.i("touch", "applied angle: $appliedAngle")
                    Log.i("touch", "|-------------------|")
                    Log.i("touch", "")

                    Log.i("TAG51", "last angle: $lastAngle")
                    Log.i("TAG51", "applied angle: $appliedAngle")
                    val diff = abs(lastAngle - appliedAngle)
                    Log.i("TAG45", "diff: $diff")
                    if (diff > 180) {
                        Log.i("TAG45", "statement")
                        Log.i("TAG45", "angle applied: $appliedAngle")
                        appliedAngle = if (appliedAngle < 180) {
                            360.0
                        } else {
                            0.0
                        }
                    }
                    val progress = appliedAngle / 360.0
                    oldProgressValue = (100 - 0) * progress


                    //Log.i("TAG55","progress: ${progress}")
                    Log.i("TAG51", "progress: ${progress}")
                    Log.i("TAG51", "oldProgressValue: ${oldProgressValue}")

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

        drawArc(
            color = progressBarColor,
            startAngle = when (startAngle) {
                StartAngle.degree_0 -> 0f
                StartAngle.degree_90 -> -90f
                StartAngle.degree_180 -> -180f
                StartAngle.degree_270 -> -270f
            },
            sweepAngle = 360f,
            useCenter = false,
            topLeft = center - Offset(radius, radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(
                width = stroke,
                cap = cap
            )
        )

        drawArc(
            color = trackColor,
            startAngle = when (startAngle) {
                StartAngle.degree_0 -> 0f
                StartAngle.degree_90 -> -90f
                StartAngle.degree_180 -> -180f
                StartAngle.degree_270 -> -270f
            },
            sweepAngle = abs(appliedAngle.toFloat()),
            topLeft = center - Offset(radius, radius),
            size = Size(radius * 2, radius * 2),
            useCenter = false,
            style = Stroke(
                width = stroke,
                cap = cap
            )
        )

        if (showProgressNumb) {

            val offset by ProgressNumbOffsetCalculation(
                startAngle = startAngle,
                radius = radius,
                appliedAngle = appliedAngle
            )

            drawCircle(
                color = numbOuterColor,
                radius = stroke,
                center = center + offset
            )

            drawCircle(
                color = numbInnerColor,
                radius = ((stroke * 2.0) / 3.0).toFloat(),
                center = center + offset
            )
        } else {

            if (oldProgressValue == 0.0) {

                val offset by StartPositionLineCalculation(
                    startAngle = startAngle,
                    radius = radius,
                    appliedAngle = appliedAngle
                )

                drawLine(
                    color = startPositionIndicatorColor,
                    strokeWidth = startPositionIndicatorStrokeWidth.toPx(),
                    start = center + offset.first,
                    end = center + offset.second
                )
            }
        }

        if (showOuterIndicatorLines) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val circleThickness = canvasWidth / 25f
            circleCenter = Offset(x = canvasWidth / 2f, y = canvasHeight / 2f)
            val outerRadius = radius + stroke / 2f
            val gap = 15f

            for (value in 0..(100 - 0)) {

                val lineColor = if (value < oldProgressValue) {
                    outerIndicatorLineColorSelected
                } else {
                    outerIndicatorLineColorUnSelected
                }

                val angleInDegrees = value * 360f / (100 - 0).toFloat()
                val adjustedAngleInDegrees = when (startAngle) {
                    StartAngle.degree_0 -> angleInDegrees - 90
                    StartAngle.degree_90 -> angleInDegrees - 180
                    StartAngle.degree_180 -> angleInDegrees - 270
                    StartAngle.degree_270 -> angleInDegrees
                }

                val angleInRadius = adjustedAngleInDegrees * PI / 180f + PI / 2f

                val yGapAdjustment = cos(x = adjustedAngleInDegrees * PI / 180f) * gap
                val xGapAdjustment = -sin(x = adjustedAngleInDegrees * PI / 180f) * gap

                val startY =
                    (outerRadius * sin(angleInRadius) + circleCenter.y + yGapAdjustment).toFloat()

                val start = Offset(
                    x = (outerRadius * cos(angleInRadius) + circleCenter.x + xGapAdjustment).toFloat(),
                    y = startY
                )

                val end = Offset(
                    x = (outerRadius * cos(angleInRadius) + circleCenter.x + xGapAdjustment).toFloat(),
                    y = (outerRadius * sin(angleInRadius) + circleThickness + circleCenter.y + yGapAdjustment).toFloat()
                )

                rotate(
                    degrees = adjustedAngleInDegrees,
                    pivot = start
                ) {
                    drawLine(
                        color = lineColor,
                        start = start,
                        end = end,
                        alpha = 0.9f,
                        strokeWidth = 2.dp.toPx()
                    )
                }

                drawContext.canvas.nativeCanvas.apply {
                    drawIntoCanvas {
                        drawText(
                            "$displayedProgressValue $valueUnit",
                            circleCenter.x,
                            circleCenter.y + 45.dp.toPx() / 3f,
                            Paint().apply {
                                customTypeFace?.let {
                                    typeface =  it
                                    textSize = textStyle.fontSize.toPx()
                                    color = textStyle.color.toArgb()
                                }?:run {
                                    textSize = 38.sp.toPx()
                                    color = Color.Black.toArgb()
                                }
                                textAlign = Paint.Align.CENTER
                                isFakeBoldText = true
                            }
                        )
                    }
                }
            }
        }
    }
}



