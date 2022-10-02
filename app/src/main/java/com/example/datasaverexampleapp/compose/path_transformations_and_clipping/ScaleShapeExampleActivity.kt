package com.example.datasaverexampleapp.compose.path_transformations_and_clipping

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale

class ScaleShapeExampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Scale shape example"
        setContent {

            // In this example we will scale shape down to half of it original size
            Canvas(modifier = Modifier.fillMaxSize()) {

                scale(scale = 0.5f, pivot = Offset(x = 200f, y = 200f)) {
                    drawRect(
                        color = Color.Red,
                        topLeft = Offset(x =100f,y = 100f),
                        size = Size(width = 200f, height = 200f)
                    )
                }
            }
        }
    }
}