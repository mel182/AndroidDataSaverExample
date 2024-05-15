package com.example.jetpackcomposedraggableslider

import android.graphics.Paint
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DraggableSlider(
    modifier: Modifier = Modifier,
    padding: Float = 0f,
    stroke: Float = 50f,
    cap: StrokeCap = StrokeCap.Round,
    touchStroke: Float = 50f,
    onChange: ((Int) -> Unit)? = null
) {

    var width by remember {
        mutableIntStateOf(0)
    }
    var height by remember {
        mutableIntStateOf(0)
    }
    var radius by remember {
        mutableFloatStateOf(0f)
    }
    var center by remember {
        mutableStateOf(Offset.Zero)
    }
    var infoCenterCircleRadius by remember {
        mutableFloatStateOf(0f)
    }
    var angle by remember {
        mutableFloatStateOf(0f)
    }
    var appliedAngle by remember {
        mutableFloatStateOf(0f)
    }
    var nearTheThumbIndicator by remember {
        mutableStateOf(false)
    }
    var displayedTemperatureValue by remember {
        mutableIntStateOf(16)
    }

    val gradient = Brush.horizontalGradient(
        colorStops = arrayOf(
            0.0f to Color(0xFF0F0FEF),
            0.2f to Color(0xFF0F0FEF),
            0.4f to Color(0xFFFF832C),
            0.2f to Color(0xFFFF832C),
            3.0f to Color(0xFFFF442C)
        )
    )

    LaunchedEffect(key1 = angle) {

        if (angle < 0.0f && angle > -90f) {
            angle = 0.0f
        } else if (angle < 0.0f && angle < -90f) {
            angle = 180.0f
        } else if (angle >= 180f) {
            angle = 180f
        }

        appliedAngle = angle
        displayedTemperatureValue = 16 + ((32-16) * (angle / 180f)).toInt()
    }

    Canvas(modifier = modifier
        .onGloballyPositioned {
            width = it.size.width
            height = it.size.height
            center = Offset(width / 2f, height / 2f)
            radius = (min(width.toFloat(), height.toFloat()) / 2f - padding - stroke / 2f)
            infoCenterCircleRadius = (radius * 0.70f)
        }
        .pointerInteropFilter {
            val x = it.x
            val y = it.y
            val offset = Offset(x = x, y = y)

            when (it.action) {

                MotionEvent.ACTION_DOWN -> {
                    val calculatedDistance = distance(first = offset, second = center)
                    val calculatedAngle = angle(center = center, offset = offset)
                    if (calculatedDistance >= radius - touchStroke / 2f && calculatedDistance <= radius + touchStroke / 2f) {
                        nearTheThumbIndicator = true
                        angle = calculatedAngle
                    } else {
                        nearTheThumbIndicator = false
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    if (nearTheThumbIndicator)
                        angle = angle(center = center, offset = offset)
                }

                MotionEvent.ACTION_UP -> {
                    onChange?.invoke(displayedTemperatureValue)
                    nearTheThumbIndicator = false
                }

                else -> return@pointerInteropFilter false
            }

            return@pointerInteropFilter true
        }
    ) {

        drawArc(
            brush = gradient,
            startAngle = -180f,
            sweepAngle = 180f,
            topLeft = center - Offset(radius, radius),
            size = Size(width = radius * 2, height = radius * 2),
            useCenter = false,
            style = Stroke(
                width = stroke,
                cap = cap
            )
        )

        drawCircle(
            color = Color(0xFF343144),
            radius = infoCenterCircleRadius,
            center = center
        )

        drawContext.canvas.nativeCanvas.apply {
            drawIntoCanvas {
                drawText(
                    "16°",
                    (center.x - radius) - 35.dp.toPx(),
                    center.y + 10.dp.toPx(),
                    Paint().apply {
                        textSize = 15.sp.toPx()
                        color = Color.White.toArgb()
                        textAlign = Paint.Align.CENTER
                        isFakeBoldText = true
                    }
                )
            }
        }

        drawContext.canvas.nativeCanvas.apply {
            drawIntoCanvas {
                drawText(
                    "32°",
                    (center.x + radius) + 35.dp.toPx(),
                    center.y + 10.dp.toPx(),
                    Paint().apply {
                        textSize = 15.sp.toPx()
                        color = Color.White.toArgb()
                        textAlign = Paint.Align.CENTER
                        isFakeBoldText = true
                    }
                )
            }
        }

        drawContext.canvas.nativeCanvas.apply {
            drawIntoCanvas {
                drawText(
                    "$displayedTemperatureValue",
                    center.x,
                    center.y - 10.sp.toPx(),
                    Paint().apply {
                        textSize = 28.sp.toPx()
                        color = Color.White.toArgb()
                        textAlign = Paint.Align.CENTER
                        isFakeBoldText = true
                    }
                )

                drawText(
                    "°C",
                    center.x + 25.dp.toPx(),
                    center.y - 35.dp.toPx(),
                    Paint().apply {
                        textSize = 12.sp.toPx()
                        color = Color(0xFF9E9EA4).toArgb()
                        textAlign = Paint.Align.CENTER
                        isFakeBoldText = false
                    }
                )

                drawText(
                    "Room",
                    center.x,
                    center.y + 30.dp.toPx(),
                    Paint().apply {
                        textSize = 14.sp.toPx()
                        color = Color(0xFF9E9EA4).toArgb()
                        textAlign = Paint.Align.CENTER
                        isFakeBoldText = false
                    }
                )

                drawText(
                    "Temperature",
                    center.x,
                    center.y + 50.dp.toPx(),
                    Paint().apply {
                        textSize = 14.sp.toPx()
                        color = Color(0xFF9E9EA4).toArgb()
                        textAlign = Paint.Align.CENTER
                        isFakeBoldText = false
                    }
                )
            }
        }

        drawCircle(
            color = Color.White,
            radius = 50f,
            center = center + Offset(
                x = radius * cos((-180 + appliedAngle) * PI / 180f).toFloat(),
                y = radius * sin((-180 + appliedAngle) * PI / 180f).toFloat()
            )
        )
    }
}

fun angle(center: Offset, offset: Offset): Float {
    val rad = atan2(center.y - offset.y, center.x - offset.x)
    val deg = Math.toDegrees(rad.toDouble())
    return deg.toFloat()
}

fun distance(first: Offset, second: Offset): Float {
    return sqrt((first.x - second.x).square() + (first.y - second.y).square())
}

fun Float.square(): Float {
    return this * this
}