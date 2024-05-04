@file:OptIn(ExperimentalComposeUiApi::class)

package com.jetpackcompose.circularindicatordraggable

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

val ColorPrimary = Color(0xFF1c2026)
val LightGreen = Color(0xFF8dc387)
val ProgressBarBg = Color(0xFFFFE9DD)
val ProgressBarProgress = Color(0xFFE08868)
val ProgressBarTint = Color(0xFFE1BAAA)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProgressBarCircular(
    modifier: Modifier = Modifier,
    startAngle: StartAngle,
    padding: Float = 50f,
    stroke: Float = 35f,
    cap: StrokeCap = StrokeCap.Round,
    minValue: Int = 0,
    maxValue: Int = 100,
    initialAngle: Double = 0.0,
    onProgressChanged: (progress: Double) -> Unit
) {
    var width by remember { mutableStateOf(0) }
    var height by remember { mutableStateOf(0) }
    var radius by remember { mutableStateOf(0f) }
    var center by remember { mutableStateOf(Offset.Zero) }

    var appliedAngle by remember {
        mutableStateOf(initialAngle)
    }
    var lastAngle by remember {
        mutableStateOf(0.0)
    }
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }
    var oldProgressValue by remember {
        mutableStateOf(initialAngle)
    }

    when (startAngle) {
        StartAngle.degree_0 -> DrawCircleAt0(
            modifier = modifier,
            padding = padding,
            stroke = stroke,
            cap = cap,
            minValue = minValue,
            maxValue = maxValue,
            initialAngle = initialAngle,
            onProgressChanged = onProgressChanged
        )

        StartAngle.degree_90 -> DrawCircleAt90(
            modifier = modifier,
            padding = padding,
            stroke = stroke,
            cap = cap,
            minValue = minValue,
            maxValue = maxValue,
            initialAngle = initialAngle,
            onProgressChanged = onProgressChanged
        )

        StartAngle.degree_180 -> DrawCircleAt180(
            modifier = modifier,
            padding = padding,
            stroke = stroke,
            cap = cap,
            minValue = minValue,
            maxValue = maxValue,
            initialAngle = initialAngle,
            onProgressChanged = onProgressChanged
        )

        StartAngle.degree_270 -> DrawCircleAt270(
            modifier = modifier,
            padding = padding,
            stroke = stroke,
            cap = cap,
            minValue = minValue,
            maxValue = maxValue,
            initialAngle = initialAngle,
            onProgressChanged = onProgressChanged
        )
    }

    return


    Canvas(modifier = modifier
        .size(270.dp)
        .onGloballyPositioned {
            width = it.size.width
            height = it.size.height
            center = Offset(width / 2f, height / 2f)
            radius = min(width.toFloat(), height.toFloat()) / 2f - padding - stroke / 2f
        }
        .pointerInteropFilter {

            when (it.action) {
                MotionEvent.ACTION_DOWN -> {

                }

                MotionEvent.ACTION_MOVE -> {
                    Log.i("TAG51", "")
                    Log.i(
                        "TAG51",
                        "delta angle: ${deltaAngle(center.x - it.x, center.y - it.y)}"
                    )
                    Log.i("TAG51", "center X: ${center.x}")
                    Log.i("TAG51", "it X: ${it.x}")
                    Log.i("TAG51", "center Y: ${center.y}")
                    Log.i("TAG51", "it Y: ${it.y}")
                    appliedAngle = if (center.x > it.x && center.y > it.y) {
                        Log.i("TAG51", "statement 1")
                        270 + deltaAngle(center.x - it.x, center.y - it.y)
                    } else {
                        Log.i("TAG51", "statement 2")
                        90 - deltaAngle(it.x - center.x, center.y - it.y)
                    }
                    Log.i("TAG51", "applied angle: $appliedAngle")

                    Log.i("TAG51", "last angle: $lastAngle")
                    Log.i("TAG51", "applied angle: $appliedAngle")
                    val diff = abs(lastAngle - appliedAngle)
                    Log.i("TAG51", "diff: $diff")
                    if (diff > 180) {
                        appliedAngle = if (appliedAngle < 180) {
                            360.0
                        } else {
                            0.0
                        }
                    }
                    val progress = appliedAngle / 360.0
                    oldProgressValue = (maxValue - minValue) * progress


                    //Log.i("TAG55","progress: ${progress}")
                    Log.i("TAG51", "progress: ${progress}")
                    Log.i("TAG51", "oldProgressValue: ${oldProgressValue}")

                    onProgressChanged(oldProgressValue)
                    lastAngle = appliedAngle

                }

                MotionEvent.ACTION_UP -> {

                }

                else -> return@pointerInteropFilter false
            }

            return@pointerInteropFilter true
        }
    ) {
        drawArc(
            color = ProgressBarBg,
            startAngle = -90f,
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
            color = ProgressBarProgress,
            startAngle = -90f,
            sweepAngle = abs(appliedAngle.toFloat()),
            topLeft = center - Offset(radius, radius),
            size = Size(radius * 2, radius * 2),
            useCenter = false,
            style = Stroke(
                width = stroke,
                cap = cap
            )
        )
//        drawCircle(
//            color = ProgressBarTint,
//            radius = stroke,
//            center = center + Offset(
//                radius * kotlin.math.cos((-90 + abs(appliedAngle)) * PI / 180f).toFloat(),
//                radius * kotlin.math.sin((-90 + abs(appliedAngle)) * PI / 180f).toFloat()
//            )
//        )

//        drawCircle(
//            color = ColorPrimary,
//            radius = ((stroke*2.0)/3.0).toFloat(),
//            center = center + Offset(
//                radius * kotlin.math.cos((-90 + abs(appliedAngle)) * PI / 180f).toFloat(),
//                radius * kotlin.math.sin((-90 + abs(appliedAngle)) * PI / 180f).toFloat()
//            )
//        )


        if (oldProgressValue == 0.0) {
            drawLine(
                color = LightGreen,
                start = center + Offset(
                    (radius - 10) * kotlin.math.cos((-90 + abs(appliedAngle)) * PI / 180f)
                        .toFloat(),
                    (radius - 10) * kotlin.math.sin((-90 + abs(appliedAngle)) * PI / 180f).toFloat()
                ),
                end = center + Offset(
                    (radius + 10) * kotlin.math.cos((-90 + abs(appliedAngle)) * PI / 180f)
                        .toFloat(),
                    (radius + 10) * kotlin.math.sin((-90 + abs(appliedAngle)) * PI / 180f).toFloat()
                )
            )
        }

        val _width = size.width
        val _height = size.height
        val circleThickness = _width / 25f
        circleCenter = Offset(x = _width / 2f, y = _height / 2f)

        val outerRadius = radius + stroke / 2f
        val gap = 15f
        for (value in 0..(maxValue - minValue)) {
            Log.i("TAG56", "")
            Log.i("TAG51", "value outer: $value")
            Log.i("TAG51", "old progress value outer: $oldProgressValue")
            Log.i("TAG51", "condition outer: ${value < oldProgressValue}")


            val color = if (value < oldProgressValue) {
                Log.i("TAG51", "RED")
                Color.Red
            } else {
                Log.i("TAG51", "BLUE")
                Color.Blue.copy(alpha = 0.3f)
            }
            //val color = if (value < oldProgressValue - minValue) Color.Blue else Color.Blue.copy(alpha = 0.3f)
            val angleInDegrees = value * 360f / (100 - 0).toFloat()
            Log.i("TAG51", "angle in degree outer 1: ${angleInDegrees}")
            Log.i("TAG56", "")

            if (angleInDegrees == 360f) {
                Log.i("TAG56", "")
                Log.i("TAG56", "full round value: $oldProgressValue")
                Log.i("TAG56", "")
            }

            val angleInDegrees2 = angleInDegrees - 180
            val angleInRadius = angleInDegrees2 * PI / 180f + PI / 2f
            //val angleInRadius = angleInDegrees * PI / 90f + PI / 2f

            val yGapAdjustment = cos(x = angleInDegrees2 * PI / 180f) * gap
            val xGapAdjustment = -sin(x = angleInDegrees2 * PI / 180f) * gap

            Log.i("TAG51", "outer radius: $outerRadius")
            Log.i("TAG51", "sin angle in radius: ${sin(angleInRadius)}")
            Log.i("TAG51", "center Y: ${circleCenter.y}")
            Log.i("TAG51", "yGapAdjustment: ${yGapAdjustment}")

            val startY =
                (outerRadius * sin(angleInRadius) + circleCenter.y + yGapAdjustment).toFloat()

//            val startY =
//                (outerRadius * sin(angleInRadius) + circleCenter.y + yGapAdjustment).toFloat()

            Log.i("TAG51", "start Y: $startY")
            Log.i("TAG51", "sin angle in radius: $angleInRadius")

            val start = Offset(
                x = (outerRadius * cos(angleInRadius) + circleCenter.x + xGapAdjustment).toFloat(),
                y = startY
            )

            val end = Offset(
                x = (outerRadius * cos(angleInRadius) + circleCenter.x + xGapAdjustment).toFloat(),
                y = (outerRadius * sin(angleInRadius) + circleThickness + circleCenter.y + yGapAdjustment).toFloat()
            )

            Log.i("TAG51", "drawing angle in degree outer: ${angleInDegrees}")

            rotate(
                degrees = angleInDegrees2,
                pivot = start
            ) {
                drawLine(
                    color = color,
                    start = start,
                    end = end,
                    alpha = 0.9f,
                    strokeWidth = 2.dp.toPx()
                )
            }
        }
    }
}

fun deltaAngle(x: Float, y: Float): Double {
    return Math.toDegrees(atan2(y.toDouble(), x.toDouble()))
}

@Composable
private fun DrawCircleAt0(
    modifier: Modifier = Modifier,
    padding: Float = 50f,
    stroke: Float = 35f,
    cap: StrokeCap = StrokeCap.Round,
    minValue: Int = 0,
    maxValue: Int = 100,
    initialAngle: Double = 0.0,
    onProgressChanged: (progress: Double) -> Unit
) {

    var width by remember { mutableStateOf(0) }
    var height by remember { mutableStateOf(0) }
    var radius by remember { mutableStateOf(0f) }
    var center by remember { mutableStateOf(Offset.Zero) }

    var appliedAngle by remember {
        mutableStateOf(initialAngle)
    }
    var lastAngle by remember {
        mutableStateOf(0.0)
    }
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }
    var oldProgressValue by remember {
        mutableStateOf(initialAngle)
    }

    Canvas(modifier = modifier
        .size(270.dp)
        .onGloballyPositioned {
            width = it.size.width
            height = it.size.height
            center = Offset(width / 2f, height / 2f)
            radius = min(width.toFloat(), height.toFloat()) / 2f - padding - stroke / 2f
        }
        .pointerInteropFilter {

            when (it.action) {
                MotionEvent.ACTION_DOWN -> {

                }

                MotionEvent.ACTION_MOVE -> {
                    Log.i("TAG51", "")
                    Log.i(
                        "TAG51",
                        "delta angle: ${deltaAngle(center.x - it.x, center.y - it.y)}"
                    )
                    Log.i("TAG45", "center X: ${center.x}")
                    Log.i("TAG45", "it X: ${it.x}")
                    Log.i("TAG45", "center Y: ${center.y}")
                    Log.i("TAG45", "it Y: ${it.y}")
                    Log.i("TAG45", "last angle: ${lastAngle}")

                    val touchAngle = -atan2(
                        x = center.y - it.y,
                        y = center.x - it.x
                    ) * (180f/PI).toFloat()
                    appliedAngle = (touchAngle + 270f).mod(360f).toDouble()
                    Log.i("touch", "applied angle 12: $appliedAngle")


                    Log.i("TAG45", "")
                    Log.i("TAG45", "|----------------------|")

                    Log.i("TAG34", "applied angle: $appliedAngle")

                    Log.i("TAG41", "last angle: $lastAngle")
                    Log.i("TAG41", "applied angle: $appliedAngle")
                    val diff = abs(lastAngle - appliedAngle)
                    Log.i("TAG34", "diff: $diff")
                    Log.i("TAG45", "angle applied: $appliedAngle")
                    Log.i("TAG45", "last angle applied: $lastAngle")
                    Log.i("TAG45", "|----------------------|")
                    Log.i("TAG45", "")

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
                    oldProgressValue = (maxValue - minValue) * progress


                    //Log.i("TAG55","progress: ${progress}")
                    Log.i("TAG51", "progress: ${progress}")
                    Log.i("TAG51", "oldProgressValue: ${oldProgressValue}")

                    onProgressChanged(oldProgressValue)
                    lastAngle = appliedAngle

                }

                MotionEvent.ACTION_UP -> {

                }

                else -> return@pointerInteropFilter false
            }

            return@pointerInteropFilter true
        }
    ) {

        Log.i("TAG34","Angle applied initial: $appliedAngle")

        drawArc(
            color = ProgressBarBg,
            startAngle = 0f,
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
            color = ProgressBarProgress,
            startAngle = 0f,
            sweepAngle = abs(appliedAngle.toFloat()),
            topLeft = center - Offset(radius, radius),
            size = Size(radius * 2, radius * 2),
            useCenter = false,
            style = Stroke(
                width = stroke,
                cap = cap
            )
        )
//        drawCircle(
//            color = ProgressBarTint,
//            radius = stroke,
//            center = center + Offset(
//                radius * kotlin.math.cos((-90 + abs(appliedAngle)) * PI / 180f).toFloat(),
//                radius * kotlin.math.sin((-90 + abs(appliedAngle)) * PI / 180f).toFloat()
//            )
//        )

//        drawCircle(
//            color = ColorPrimary,
//            radius = ((stroke*2.0)/3.0).toFloat(),
//            center = center + Offset(
//                radius * kotlin.math.cos((-90 + abs(appliedAngle)) * PI / 180f).toFloat(),
//                radius * kotlin.math.sin((-90 + abs(appliedAngle)) * PI / 180f).toFloat()
//            )
//        )


        if (oldProgressValue == 0.0) {
            drawLine(
                color = LightGreen,
                start = center + Offset(
                    (radius - 10) * cos((abs(appliedAngle)) * PI / 180f)
                        .toFloat(),
                    (radius - 10) * sin((abs(appliedAngle)) * PI / 180f).toFloat()
                ),
                end = center + Offset(
                    (radius + 10) * cos((abs(appliedAngle)) * PI / 180f)
                        .toFloat(),
                    (radius + 10) * sin((abs(appliedAngle)) * PI / 180f).toFloat()
                )
            )
        }


        val _width = size.width
        val _height = size.height
        val circleThickness = _width / 25f
        circleCenter = Offset(x = _width / 2f, y = _height / 2f)

        val outerRadius = radius + stroke / 2f
        val gap = 15f
        for (value in 0..(maxValue - minValue)) {
            Log.i("TAG60", "")
            Log.i("TAG60", "value outer: $value")
            Log.i("TAG60", "old progress value outer: $oldProgressValue")
            Log.i("TAG60", "condition outer: ${value < oldProgressValue}")

            val color = if (value < oldProgressValue) {
                Log.i("TAG60", "RED")
                Color.Red
            } else {
                Log.i("TAG60", "BLUE")
                Color.Blue.copy(alpha = 0.3f)
            }
            //val color = if (value < oldProgressValue - minValue) Color.Blue else Color.Blue.copy(alpha = 0.3f)
            val angleInDegrees = value * 360f / (100 - 0).toFloat()
            Log.i("TAG60", "angle in degree outer 1: ${angleInDegrees}")
            Log.i("TAG60", "")

            val angleInDegrees2 = angleInDegrees - 90
            val angleInRadius = angleInDegrees2 * PI / 180f + PI / 2f

            val yGapAdjustment = cos(x = angleInDegrees2 * PI / 180f) * gap
            val xGapAdjustment = -sin(x = angleInDegrees2 * PI / 180f) * gap

            Log.i("TAG60", "outer radius: $outerRadius")
            Log.i("TAG60", "sin angle in radius: ${sin(angleInRadius)}")
            Log.i("TAG60", "center Y: ${circleCenter.y}")
            Log.i("TAG60", "yGapAdjustment: ${yGapAdjustment}")

            val startY =
                (outerRadius * sin(angleInRadius) + circleCenter.y + yGapAdjustment).toFloat()
            
            Log.i("TAG60", "start Y: $startY")
            Log.i("TAG60", "sin angle in radius: $angleInRadius")

            val start = Offset(
                x = (outerRadius * cos(angleInRadius) + circleCenter.x + xGapAdjustment).toFloat(),
                y = startY
            )

            val end = Offset(
                x = (outerRadius * cos(angleInRadius) + circleCenter.x + xGapAdjustment).toFloat(),
                y = (outerRadius * sin(angleInRadius) + circleThickness + circleCenter.y + yGapAdjustment).toFloat()
            )

            Log.i("TAG60", "drawing angle in degree outer: ${angleInDegrees}")

            rotate(
                degrees = angleInDegrees2,
                pivot = start
            ) {
                drawLine(
                    color = color,
                    start = start,
                    end = end,
                    alpha = 0.9f,
                    strokeWidth = 2.dp.toPx()
                )
            }
        }
    }
}

@Composable
private fun DrawCircleAt90(
    modifier: Modifier = Modifier,
    padding: Float = 50f,
    stroke: Float = 35f,
    cap: StrokeCap = StrokeCap.Round,
    minValue: Int = 0,
    rotate:Float = 0.0f,
    maxValue: Int = 100,
    initialAngle: Double = 0.0,
    onProgressChanged: (progress: Double) -> Unit
) {

    var width by remember { mutableStateOf(0) }
    var height by remember { mutableStateOf(0) }
    var radius by remember { mutableStateOf(0f) }
    var center by remember { mutableStateOf(Offset.Zero) }

    var appliedAngle by remember {
        mutableStateOf(initialAngle)
    }
    var lastAngle by remember {
        mutableStateOf(0.0)
    }
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }
    var oldProgressValue by remember {
        mutableStateOf(initialAngle)
    }

    Canvas(modifier = modifier
        .size(270.dp).then(
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
                MotionEvent.ACTION_DOWN -> {

                }

                MotionEvent.ACTION_MOVE -> {
                    Log.i("TAG51", "")
                    Log.i(
                        "TAG51",
                        "delta angle: ${deltaAngle(center.x - it.x, center.y - it.y)}"
                    )
                    Log.i("TAG51", "center X: ${center.x}")
                    Log.i("TAG51", "it X: ${it.x}")
                    Log.i("TAG51", "center Y: ${center.y}")
                    Log.i("TAG51", "it Y: ${it.y}")

                    val touchAngle = -atan2(
                        x = center.y - it.y,
                        y = center.x - it.x
                    ) * (180f/PI).toFloat()
                    //) * (180f / PI).toFloat()
                    //val appliedAngle12 = (touchAngle + 180f).mod(360f).toDouble()
                    appliedAngle = (touchAngle).mod(360f).toDouble()
                    Log.i("touch", "applied angle 12: $appliedAngle")

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
                    oldProgressValue = (maxValue - minValue) * progress


                    //Log.i("TAG55","progress: ${progress}")
                    Log.i("TAG51", "progress: ${progress}")
                    Log.i("TAG51", "oldProgressValue: ${oldProgressValue}")

                    onProgressChanged(oldProgressValue)
                    lastAngle = appliedAngle

                }

                MotionEvent.ACTION_UP -> {

                }

                else -> return@pointerInteropFilter false
            }

            return@pointerInteropFilter true
        }
    ) {
        drawArc(
            color = ProgressBarBg,
            startAngle = -90f,
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
            color = ProgressBarProgress,
            startAngle = -90f,
            sweepAngle = abs(appliedAngle.toFloat()),
            topLeft = center - Offset(radius, radius),
            size = Size(radius * 2, radius * 2),
            useCenter = false,
            style = Stroke(
                width = stroke,
                cap = cap
            )
        )
//        drawCircle(
//            color = ProgressBarTint,
//            radius = stroke,
//            center = center + Offset(
//                radius * kotlin.math.cos((-90 + abs(appliedAngle)) * PI / 180f).toFloat(),
//                radius * kotlin.math.sin((-90 + abs(appliedAngle)) * PI / 180f).toFloat()
//            )
//        )

//        drawCircle(
//            color = ColorPrimary,
//            radius = ((stroke*2.0)/3.0).toFloat(),
//            center = center + Offset(
//                radius * kotlin.math.cos((-90 + abs(appliedAngle)) * PI / 180f).toFloat(),
//                radius * kotlin.math.sin((-90 + abs(appliedAngle)) * PI / 180f).toFloat()
//            )
//        )


        if (oldProgressValue == 0.0) {
            drawLine(
                color = LightGreen,
                start = center + Offset(
                    (radius - 10) * kotlin.math.cos((-90 + abs(appliedAngle)) * PI / 180f)
                        .toFloat(),
                    (radius - 10) * kotlin.math.sin((-90 + abs(appliedAngle)) * PI / 180f).toFloat()
                ),
                end = center + Offset(
                    (radius + 10) * kotlin.math.cos((-90 + abs(appliedAngle)) * PI / 180f)
                        .toFloat(),
                    (radius + 10) * kotlin.math.sin((-90 + abs(appliedAngle)) * PI / 180f).toFloat()
                )
            )
        }

        val _width = size.width
        val _height = size.height
        val circleThickness = _width / 25f
        circleCenter = Offset(x = _width / 2f, y = _height / 2f)

        val outerRadius = radius + stroke / 2f
        val gap = 15f
        for (value in 0..(maxValue - minValue)) {
            Log.i("TAG56", "")
            Log.i("TAG51", "value outer: $value")
            Log.i("TAG51", "old progress value outer: $oldProgressValue")
            Log.i("TAG51", "condition outer: ${value < oldProgressValue}")


            val color = if (value < oldProgressValue) {
                Log.i("TAG51", "RED")
                Color.Red
            } else {
                Log.i("TAG51", "BLUE")
                Color.Blue.copy(alpha = 0.3f)
            }
            //val color = if (value < oldProgressValue - minValue) Color.Blue else Color.Blue.copy(alpha = 0.3f)
            val angleInDegrees = value * 360f / (100 - 0).toFloat()
            Log.i("TAG51", "angle in degree outer 1: ${angleInDegrees}")
            Log.i("TAG56", "")

            if (angleInDegrees == 360f) {
                Log.i("TAG56", "")
                Log.i("TAG56", "full round value: $oldProgressValue")
                Log.i("TAG56", "")
            }

            val angleInDegrees2 = angleInDegrees - 180
            val angleInRadius = angleInDegrees2 * PI / 180f + PI / 2f
            //val angleInRadius = angleInDegrees * PI / 90f + PI / 2f

            val yGapAdjustment = cos(x = angleInDegrees2 * PI / 180f) * gap
            val xGapAdjustment = -sin(x = angleInDegrees2 * PI / 180f) * gap

            Log.i("TAG51", "outer radius: $outerRadius")
            Log.i("TAG51", "sin angle in radius: ${sin(angleInRadius)}")
            Log.i("TAG51", "center Y: ${circleCenter.y}")
            Log.i("TAG51", "yGapAdjustment: ${yGapAdjustment}")

            val startY =
                (outerRadius * sin(angleInRadius) + circleCenter.y + yGapAdjustment).toFloat()

//            val startY =
//                (outerRadius * sin(angleInRadius) + circleCenter.y + yGapAdjustment).toFloat()

            Log.i("TAG51", "start Y: $startY")
            Log.i("TAG51", "sin angle in radius: $angleInRadius")

            val start = Offset(
                x = (outerRadius * cos(angleInRadius) + circleCenter.x + xGapAdjustment).toFloat(),
                y = startY
            )

            val end = Offset(
                x = (outerRadius * cos(angleInRadius) + circleCenter.x + xGapAdjustment).toFloat(),
                y = (outerRadius * sin(angleInRadius) + circleThickness + circleCenter.y + yGapAdjustment).toFloat()
            )

            Log.i("TAG51", "drawing angle in degree outer: ${angleInDegrees}")

            rotate(
                degrees = angleInDegrees2,
                pivot = start
            ) {
                drawLine(
                    color = color,
                    start = start,
                    end = end,
                    alpha = 0.9f,
                    strokeWidth = 2.dp.toPx()
                )
            }
        }
    }
}

@Composable
private fun DrawCircleAt180(
    modifier: Modifier = Modifier,
    padding: Float = 50f,
    stroke: Float = 35f,
    cap: StrokeCap = StrokeCap.Round,
    minValue: Int = 0,
    rotate:Float = 0.0f,
    maxValue: Int = 100,
    initialAngle: Double = 0.0,
    onProgressChanged: (progress: Double) -> Unit
) {

    var width by remember { mutableStateOf(0) }
    var height by remember { mutableStateOf(0) }
    var radius by remember { mutableStateOf(0f) }
    var center by remember { mutableStateOf(Offset.Zero) }

    var appliedAngle by remember {
        mutableStateOf(initialAngle)
    }
    var lastAngle by remember {
        mutableStateOf(0.0)
    }
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }
    var oldProgressValue by remember {
        mutableStateOf(initialAngle)
    }

    Canvas(modifier = modifier
        .size(270.dp).then(
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
                MotionEvent.ACTION_DOWN -> {

                }

                MotionEvent.ACTION_MOVE -> {
                    Log.i("TAG51", "")
                    Log.i(
                        "TAG51",
                        "delta angle: ${deltaAngle(center.x - it.x, center.y - it.y)}"
                    )
                    Log.i("TAG34", "")
                    Log.i("TAG34", "|-------------------|")
                    Log.i("TAG34", "center X: ${center.x}")
                    Log.i("TAG34", "it X: ${it.x}")
                    Log.i("TAG34", "center Y: ${center.y}")
                    Log.i("TAG34", "it Y: ${it.y}")

                    val touchAngle = -atan2(
                        x = center.y - it.y,
                        y = center.x - it.x
                    ) * (180f/PI).toFloat()
                    //) * (180f / PI).toFloat()
                    //val appliedAngle12 = (touchAngle + 180f).mod(360f).toDouble()
                    appliedAngle = (touchAngle + 90f).mod(360f).toDouble()
                    Log.i("touch", "applied angle 12: $appliedAngle")


                    Log.i("TAG34", "applied angle: $appliedAngle")
                    Log.i("TAG34", "|-------------------|")
                    Log.i("TAG34", "")

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
                    oldProgressValue = (maxValue - minValue) * progress


                    //Log.i("TAG55","progress: ${progress}")
                    Log.i("TAG51", "progress: ${progress}")
                    Log.i("TAG51", "oldProgressValue: ${oldProgressValue}")

                    onProgressChanged(oldProgressValue)
                    lastAngle = appliedAngle

                }

                MotionEvent.ACTION_UP -> {

                }

                else -> return@pointerInteropFilter false
            }

            return@pointerInteropFilter true
        }
    ) {
        drawArc(
            color = ProgressBarBg,
            startAngle = -180f,
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
            color = ProgressBarProgress,
            startAngle = -180f,
            sweepAngle = abs(appliedAngle.toFloat()),
            topLeft = center - Offset(radius, radius),
            size = Size(radius * 2, radius * 2),
            useCenter = false,
            style = Stroke(
                width = stroke,
                cap = cap
            )
        )
//        drawCircle(
//            color = ProgressBarTint,
//            radius = stroke,
//            center = center + Offset(
//                radius * kotlin.math.cos((-90 + abs(appliedAngle)) * PI / 180f).toFloat(),
//                radius * kotlin.math.sin((-90 + abs(appliedAngle)) * PI / 180f).toFloat()
//            )
//        )

//        drawCircle(
//            color = ColorPrimary,
//            radius = ((stroke*2.0)/3.0).toFloat(),
//            center = center + Offset(
//                radius * kotlin.math.cos((-90 + abs(appliedAngle)) * PI / 180f).toFloat(),
//                radius * kotlin.math.sin((-90 + abs(appliedAngle)) * PI / 180f).toFloat()
//            )
//        )


        if (oldProgressValue == 0.0) {
            drawLine(
                color = LightGreen,
                strokeWidth = 2.dp.toPx(),
                start = center + Offset(
                    (radius - 10) * cos((-180 + abs(appliedAngle)) * PI / 180f)
                        .toFloat(),
                    (radius - 10) * sin((-180 + abs(appliedAngle)) * PI / 180f).toFloat()
                ),
                end = center + Offset(
                    (radius + 10) * cos((-180 + abs(appliedAngle)) * PI / 180f)
                        .toFloat(),
                    (radius + 10) * sin((-180 + abs(appliedAngle)) * PI / 180f).toFloat()
                )
            )
        }

        val _width = size.width
        val _height = size.height
        val circleThickness = _width / 25f
        circleCenter = Offset(x = _width / 2f, y = _height / 2f)

        val outerRadius = radius + stroke / 2f
        val gap = 15f
        for (value in 0..(maxValue - minValue)) {
            Log.i("TAG56", "")
            Log.i("TAG51", "value outer: $value")
            Log.i("TAG51", "old progress value outer: $oldProgressValue")
            Log.i("TAG51", "condition outer: ${value < oldProgressValue}")


            val color = if (value < oldProgressValue) {
                Log.i("TAG51", "RED")
                Color.Red
            } else {
                Log.i("TAG51", "BLUE")
                Color.Blue.copy(alpha = 0.3f)
            }
            //val color = if (value < oldProgressValue - minValue) Color.Blue else Color.Blue.copy(alpha = 0.3f)
            val angleInDegrees = (value * 360f / (100 - 0).toFloat()) + 270
            Log.i("TAG51", "angle in degree outer 1: ${angleInDegrees}")
            Log.i("TAG56", "")

//            if (angleInDegrees == 360f) {
//                Log.i("TAG56", "")
//                Log.i("TAG56", "full round value: $oldProgressValue")
//                Log.i("TAG56", "")
//            }

            val angleInDegrees2 = angleInDegrees - 180
            val angleInRadius = angleInDegrees2 * PI / 180f + PI / 2f
            //val angleInRadius = angleInDegrees * PI / 90f + PI / 2f

            val yGapAdjustment = cos(x = angleInDegrees2 * PI / 180f) * gap
            val xGapAdjustment = -sin(x = angleInDegrees2 * PI / 180f) * gap

            Log.i("TAG51", "outer radius: $outerRadius")
            Log.i("TAG51", "sin angle in radius: ${sin(angleInRadius)}")
            Log.i("TAG51", "center Y: ${circleCenter.y}")
            Log.i("TAG51", "yGapAdjustment: ${yGapAdjustment}")

            val startY =
                (outerRadius * sin(angleInRadius) + circleCenter.y + yGapAdjustment).toFloat()

//            val startY =
//                (outerRadius * sin(angleInRadius) + circleCenter.y + yGapAdjustment).toFloat()

            Log.i("TAG51", "start Y: $startY")
            Log.i("TAG51", "sin angle in radius: $angleInRadius")

            val start = Offset(
                x = (outerRadius * cos(angleInRadius) + circleCenter.x + xGapAdjustment).toFloat(),
                y = startY
            )

            val end = Offset(
                x = (outerRadius * cos(angleInRadius) + circleCenter.x + xGapAdjustment).toFloat(),
                y = (outerRadius * sin(angleInRadius) + circleThickness + circleCenter.y + yGapAdjustment).toFloat()
            )

            Log.i("TAG51", "drawing angle in degree outer: ${angleInDegrees}")

            rotate(
                degrees = angleInDegrees2,
                pivot = start
            ) {
                drawLine(
                    color = color,
                    start = start,
                    end = end,
                    alpha = 0.9f,
                    strokeWidth = 2.dp.toPx()
                )
            }
        }
    }
}

@Composable
private fun DrawCircleAt270(
    modifier: Modifier = Modifier,
    padding: Float = 50f,
    stroke: Float = 35f,
    cap: StrokeCap = StrokeCap.Round,
    minValue: Int = 0,
    rotate:Float = 0.0f,
    maxValue: Int = 100,
    initialAngle: Double = 0.0,
    onProgressChanged: (progress: Double) -> Unit
) {

    var width by remember { mutableStateOf(0) }
    var height by remember { mutableStateOf(0) }
    var radius by remember { mutableStateOf(0f) }
    var center by remember { mutableStateOf(Offset.Zero) }

    var appliedAngle by remember {
        mutableStateOf(initialAngle)
    }
    var lastAngle by remember {
        mutableStateOf(0.0)
    }
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }
    var oldProgressValue by remember {
        mutableStateOf(initialAngle)
    }

    Canvas(modifier = modifier
        .size(270.dp).then(
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
                MotionEvent.ACTION_DOWN -> {

                }

                MotionEvent.ACTION_MOVE -> {
                    Log.i("TAG51", "")
                    Log.i(
                        "TAG51",
                        "delta angle: ${deltaAngle(center.x - it.x, center.y - it.y)}"
                    )
                    Log.i("TAG34", "")
                    Log.i("TAG34", "|-------------------|")
                    Log.i("TAG34", "center X: ${center.x}")
                    Log.i("TAG34", "it X: ${it.x}")
                    Log.i("TAG34", "center Y: ${center.y}")
                    Log.i("TAG34", "it Y: ${it.y}")

                    val touchAngle = -atan2(
                        x = center.y - it.y,
                        y = center.x - it.x
                    ) * (180f / PI).toFloat()
                    appliedAngle = (touchAngle + 180f).mod(360f).toDouble()

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
                    oldProgressValue = (maxValue - minValue) * progress


                    //Log.i("TAG55","progress: ${progress}")
                    Log.i("TAG51", "progress: ${progress}")
                    Log.i("TAG51", "oldProgressValue: ${oldProgressValue}")

                    onProgressChanged(oldProgressValue)
                    lastAngle = appliedAngle

                }

                MotionEvent.ACTION_UP -> {

                }

                else -> return@pointerInteropFilter false
            }

            return@pointerInteropFilter true
        }
    ) {
        drawArc(
            color = ProgressBarBg,
            startAngle = -270f,
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
            color = ProgressBarProgress,
            startAngle = -270f,
            sweepAngle = abs(appliedAngle.toFloat()),
            topLeft = center - Offset(radius, radius),
            size = Size(radius * 2, radius * 2),
            useCenter = false,
            style = Stroke(
                width = stroke,
                cap = cap
            )
        )
//        drawCircle(
//            color = ProgressBarTint,
//            radius = stroke,
//            center = center + Offset(
//                radius * kotlin.math.cos((-90 + abs(appliedAngle)) * PI / 180f).toFloat(),
//                radius * kotlin.math.sin((-90 + abs(appliedAngle)) * PI / 180f).toFloat()
//            )
//        )

//        drawCircle(
//            color = ColorPrimary,
//            radius = ((stroke*2.0)/3.0).toFloat(),
//            center = center + Offset(
//                radius * kotlin.math.cos((-90 + abs(appliedAngle)) * PI / 180f).toFloat(),
//                radius * kotlin.math.sin((-90 + abs(appliedAngle)) * PI / 180f).toFloat()
//            )
//        )


        if (oldProgressValue == 0.0) {
            drawLine(
                color = LightGreen,
                strokeWidth = 2.dp.toPx(),
                start = center + Offset(
                    (radius - 10) * cos((-270 + abs(appliedAngle)) * PI / 180f)
                        .toFloat(),
                    (radius - 10) * sin((-270 + abs(appliedAngle)) * PI / 180f).toFloat()
                ),
                end = center + Offset(
                    (radius + 10) * cos((-270 + abs(appliedAngle)) * PI / 180f)
                        .toFloat(),
                    (radius + 10) * sin((-270 + abs(appliedAngle)) * PI / 180f).toFloat()
                )
            )
        }

        val _width = size.width
        val _height = size.height
        val circleThickness = _width / 25f
        circleCenter = Offset(x = _width / 2f, y = _height / 2f)

        val outerRadius = radius + stroke / 2f
        val gap = 15f
        for (value in 0..(maxValue - minValue)) {
            Log.i("TAG56", "")
            Log.i("TAG51", "value outer: $value")
            Log.i("TAG51", "old progress value outer: $oldProgressValue")
            Log.i("TAG51", "condition outer: ${value < oldProgressValue}")


            val color = if (value < oldProgressValue) {
                Log.i("TAG51", "RED")
                Color.Red
            } else {
                Log.i("TAG51", "BLUE")
                Color.Blue.copy(alpha = 0.3f)
            }
            //val color = if (value < oldProgressValue - minValue) Color.Blue else Color.Blue.copy(alpha = 0.3f)
            val angleInDegrees = (value * 360f / (100 - 0).toFloat()) + 270
            Log.i("TAG51", "angle in degree outer 1: ${angleInDegrees}")
            Log.i("TAG56", "")

//            if (angleInDegrees == 360f) {
//                Log.i("TAG56", "")
//                Log.i("TAG56", "full round value: $oldProgressValue")
//                Log.i("TAG56", "")
//            }

            val angleInDegrees2 = angleInDegrees - 270
            val angleInRadius = angleInDegrees2 * PI / 180f + PI / 2f
            //val angleInRadius = angleInDegrees * PI / 90f + PI / 2f

            val yGapAdjustment = cos(x = angleInDegrees2 * PI / 180f) * gap
            val xGapAdjustment = -sin(x = angleInDegrees2 * PI / 180f) * gap

            Log.i("TAG51", "outer radius: $outerRadius")
            Log.i("TAG51", "sin angle in radius: ${sin(angleInRadius)}")
            Log.i("TAG51", "center Y: ${circleCenter.y}")
            Log.i("TAG51", "yGapAdjustment: ${yGapAdjustment}")

            val startY =
                (outerRadius * sin(angleInRadius) + circleCenter.y + yGapAdjustment).toFloat()

//            val startY =
//                (outerRadius * sin(angleInRadius) + circleCenter.y + yGapAdjustment).toFloat()

            Log.i("TAG51", "start Y: $startY")
            Log.i("TAG51", "sin angle in radius: $angleInRadius")

            val start = Offset(
                x = (outerRadius * cos(angleInRadius) + circleCenter.x + xGapAdjustment).toFloat(),
                y = startY
            )

            val end = Offset(
                x = (outerRadius * cos(angleInRadius) + circleCenter.x + xGapAdjustment).toFloat(),
                y = (outerRadius * sin(angleInRadius) + circleThickness + circleCenter.y + yGapAdjustment).toFloat()
            )

            Log.i("TAG51", "drawing angle in degree outer: ${angleInDegrees}")

            rotate(
                degrees = angleInDegrees2,
                pivot = start
            ) {
                drawLine(
                    color = color,
                    start = start,
                    end = end,
                    alpha = 0.9f,
                    strokeWidth = 2.dp.toPx()
                )
            }
        }
    }
}

