package com.jetpackcompose.circularindicatordraggable.feature.domain

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jetpackcompose.circularindicatordraggable.feature.data.StartAngle
import com.jetpackcompose.circularindicatordraggable.feature.data.CircularProgressColors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun DrawScope.drawProgressBar(
    color: Color,
    startAngle: StartAngle,
    topLeft: Offset,
    size: Size,
    style: DrawStyle
) {

    drawArc(
        color = color,
        startAngle = when (startAngle) {
            StartAngle.degree_0 -> 0f
            StartAngle.degree_90 -> -90f
            StartAngle.degree_180 -> -180f
            StartAngle.degree_270 -> -270f
        },
        sweepAngle = 360f,
        useCenter = false,
        topLeft = topLeft,
        size = size,
        style = style
    )
}

fun DrawScope.drawProgressTrack(
    color: Color,
    startAngle: StartAngle,
    sweepAngle: Float,
    topLeft: Offset,
    size: Size,
    style: DrawStyle
) {
    drawArc(
        color = color,
        startAngle = when (startAngle) {
            StartAngle.degree_0 -> 0f
            StartAngle.degree_90 -> -90f
            StartAngle.degree_180 -> -180f
            StartAngle.degree_270 -> -270f
        },
        sweepAngle = sweepAngle,
        topLeft = topLeft,
        size = size,
        useCenter = false,
        style = style
    )
}

fun DrawScope.drawProgressNumb(
    colorScheme: CircularProgressColors,
    startAngle: StartAngle,
    center: Offset,
    radius: Float,
    appliedAngle: Double,
    stroke: Float
) {
    val offset by ProgressNumbOffsetCalculation(
        startAngle = startAngle,
        radius = radius,
        appliedAngle = appliedAngle
    )

    drawCircle(
        color = colorScheme.numbOuterColor,
        radius = stroke,
        center = center + offset
    )

    drawCircle(
        color = colorScheme.numbInnerColor,
        radius = ((stroke * 2.0) / 3.0).toFloat(),
        center = center + offset
    )
}

fun DrawScope.drawStartPositionIndicatorLine(
    colorScheme: CircularProgressColors,
    startAngle: StartAngle,
    startPositionIndicatorStrokeWidth: Dp,
    center: Offset,
    radius: Float,
    appliedAngle: Double
) {
    val offset by StartPositionLineCalculation(
        startAngle = startAngle,
        radius = radius,
        appliedAngle = appliedAngle
    )

    drawLine(
        color = colorScheme.startPositionIndicatorColor,
        strokeWidth = startPositionIndicatorStrokeWidth.toPx(),
        start = center + offset.first,
        end = center + offset.second
    )
}


fun DrawScope.drawOuterIndicatorLines(
    stroke: Float,
    circleCenter: MutableState<Offset>,
    colorScheme: CircularProgressColors,
    startAngle: StartAngle,
    oldProgressValue: Double,
    textStyle: TextStyle,
    typeFace: Typeface,
    displayText: String,
    radius: Float
) {

    val canvasWidth = size.width
    val canvasHeight = size.height
    val circleThickness = canvasWidth / 25f
    circleCenter.value = Offset(x = canvasWidth / 2f, y = canvasHeight / 2f)
    val outerRadius = radius + stroke / 2f
    val gap = 15f

    for (value in 0..(100 - 0)) {

        val lineColor = if (value < oldProgressValue) {
            colorScheme.outerIndicatorSelectedLineColor
        } else {
            colorScheme.outerIndicatorUnSelectedLineColor
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
            (outerRadius * sin(angleInRadius) + circleCenter.value.y + yGapAdjustment).toFloat()

        val start = Offset(
            x = (outerRadius * cos(angleInRadius) + circleCenter.value.x + xGapAdjustment).toFloat(),
            y = startY
        )

        val end = Offset(
            x = (outerRadius * cos(angleInRadius) + circleCenter.value.x + xGapAdjustment).toFloat(),
            y = (outerRadius * sin(angleInRadius) + circleThickness + circleCenter.value.y + yGapAdjustment).toFloat()
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
                    displayText,
                    circleCenter.value.x,
                    circleCenter.value.y + 45.dp.toPx() / 3f,
                    Paint().apply {
                        typeface = typeFace
                        textSize = textStyle.fontSize.toPx()
                        color = textStyle.color.toArgb()
                        textAlign = Paint.Align.CENTER
                        isFakeBoldText = true
                    }
                )
            }
        }
    }
}