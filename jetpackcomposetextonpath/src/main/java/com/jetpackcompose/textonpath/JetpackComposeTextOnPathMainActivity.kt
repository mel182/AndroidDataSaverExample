package com.jetpackcompose.textonpath

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.nativeCanvas

class JetpackComposeTextOnPathMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Text on path Example"
        setContent {
            val path = Path().apply {
                moveTo(x = 200f, y = 800f)
                quadraticBezierTo(x1 = 600f, y1 = 400f, x2 = 1000f, y2 = 800f)
            }
            // This example show a write text on a path
            Canvas(modifier = Modifier.fillMaxSize()) {

                // to draw the path enable this line
//                    drawPath(
//                        path = path,
//                        color = Color.Blue,
//                        style = Stroke(width = 1.dp.toPx())
//                    )

                drawContext.canvas.nativeCanvas.apply {
                    drawTextOnPath(
                        "Hello World!",
                        path.asAndroidPath(),
                        30f,
                        50f,
                        Paint().apply {
                            color = Color.RED
                            textSize = 70f
                            textAlign = Paint.Align.CENTER
                        }
                    )
                }
            }
        }
    }
}