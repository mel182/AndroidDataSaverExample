package com.jetpackcompose.circularindicatordraggable

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Slider() {
    var radius by remember {
        mutableStateOf(0f)
    }

    var shapeCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    var handleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    var angle by remember {
        mutableStateOf(20.0)
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->

                    if (angle == 360.0) {

                        return@detectDragGestures
                    }

                    val angle1 = getRotationAngle(handleCenter, shapeCenter)
                    Log.i("TAG45","requested angle: $angle1")

                    handleCenter += dragAmount

                    angle = getRotationAngle(handleCenter, shapeCenter)
                    change.consume()
                }
            }
            .padding(30.dp)

    ) {
        shapeCenter = center

        radius = size.minDimension / 2

        val x = (shapeCenter.x + cos(Math.toRadians(angle)) * radius).toFloat()
        val y = (shapeCenter.y + sin(Math.toRadians(angle)) * radius).toFloat()

        handleCenter = Offset(x, y)

        drawCircle(color = Color.Black.copy(alpha = 0.10f), style = Stroke(20f), radius = radius)
        drawArc(
            color = Color.Yellow,
            startAngle = 0f,
            sweepAngle = angle.toFloat(),
            useCenter = false,
            style = Stroke(20f)
        )

        drawCircle(color = Color.Cyan, center = handleCenter, radius = 60f)
    }
}

private fun getRotationAngle(currentPosition: Offset, center: Offset): Double {
    val (dx, dy) = currentPosition - center
    val theta = atan2(dy, dx).toDouble()

    var angle = Math.toDegrees(theta)

    if (angle < 0) {
        angle += 360.0
    }
    return angle
}