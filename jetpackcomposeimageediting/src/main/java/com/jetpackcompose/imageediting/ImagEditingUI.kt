package com.jetpackcompose.imageediting

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.jetpackcompose.imageediting.type_alias.Drawable

@Composable
fun ImageEditing() {

    val ferrariF1 = ImageBitmap.imageResource(id = Drawable.ferrari_f1)
    Canvas(modifier = Modifier.fillMaxSize()) {

//                drawImage(
//                    image = ferrariF1,
//                    dstOffset = IntOffset(100,100),
//                    dstSize = IntSize(
//                        (400 * (ferrariF1.width.toFloat() / ferrariF1.height)).toInt(),
//                        400
//                    )
//                )
        // Blend mode color (circle)
//                drawCircle(
//                    color = Color.Red,
//                    radius = 200f,
//                    center = Offset(300f,300f),
//                    blendMode = BlendMode.Color
//                )
        // Blend mode screen (circle)
//                drawCircle(
//                    color = Color.Red,
//                    radius = 200f,
//                    center = Offset(300f,300f),
//                    blendMode = BlendMode.Screen
//                )
        // Blend mode exclusion (circle)
//                drawCircle(
//                    color = Color.Red,
//                    radius = 200f,
//                    center = Offset(300f,300f),
//                    blendMode = BlendMode.Exclusion
//                )

        // Apply blend mode to image
        drawCircle(
            color = Color.Red,
            radius = 200f,
            center = Offset(300f,300f)
        )

        drawImage(
            image = ferrariF1,
            dstOffset = IntOffset(100,100),
            dstSize = IntSize(
                (400 * (ferrariF1.width.toFloat() / ferrariF1.height)).toInt(),
                400
            ),
            blendMode = BlendMode.Exclusion
        )

    }

}