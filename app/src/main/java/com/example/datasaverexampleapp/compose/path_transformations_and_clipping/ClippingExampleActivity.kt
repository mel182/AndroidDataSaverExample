package com.example.datasaverexampleapp.compose.path_transformations_and_clipping

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.dp

class ClippingExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Clipping example"
        setContent {

            // Clipping prevent content from overlap a shape,
            // in this case the black circle
            Canvas(modifier = Modifier.fillMaxSize()) {

                val circle = Path().apply {
                    addOval(Rect(center = Offset(400f, 400f), radius = 300f))
                }

                drawPath(
                    path = circle,
                    color = Color.Black,
                    style = Stroke(width = 5.dp.toPx())
                )

                clipPath(
                    path = circle
                ) {
                    drawRect(
                        color = Color.Red,
                        topLeft = Offset(x =400f,y = 400f),
                        size = Size(width = 400f, height = 400f)
                    )
                }
            }
        }
    }
}