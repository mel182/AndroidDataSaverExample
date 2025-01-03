package com.jetpackcompose.imageediting2

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.jetpackcompose.imageediting2.type_alias.Drawable
import kotlin.math.roundToInt

@Composable
fun ImageEditingExerciseUI() {

    var circlePosition by remember {
        mutableStateOf(Offset.Zero)
    }
    var oldCirclePosition by remember {
        mutableStateOf(Offset.Zero)
    }

    val ferrariF1 = ImageBitmap.imageResource(id = Drawable.ferrari_f1)
    val radius = 300f
    Canvas(modifier = Modifier.fillMaxSize().pointerInput(key1 = true){
        detectDragGestures(onDragEnd = {
            oldCirclePosition = circlePosition
        }) {
                change, dragAmount ->
            circlePosition = oldCirclePosition + change.position
        }
    }) {

        val bitmapHeight = ((ferrariF1.height.toFloat() / ferrariF1.width.toFloat()) * size.width).roundToInt()
        val circlePath = Path().apply {
            addArc(
                oval = Rect(circlePosition, radius),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 360f
            )
        }
        drawImage(image = ferrariF1,
            dstSize = IntSize(size.width.roundToInt(), bitmapHeight),
            dstOffset = IntOffset(0, center.y.roundToInt() - bitmapHeight / 2),
            colorFilter = ColorFilter.tint(Color.Black, BlendMode.Color)
        )

        clipPath(circlePath, clipOp = ClipOp.Intersect) {
            drawImage(image = ferrariF1,
                dstSize = IntSize(size.width.roundToInt(), bitmapHeight),
                dstOffset = IntOffset(0, center.y.roundToInt() - bitmapHeight / 2)
            )
        }
    }
}