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
import androidx.compose.ui.graphics.drawscope.rotate

class RotateRectangleExampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Path rotate rectangle example"
        setContent {
            // This example shows how to rotate an rectangle 45 degrees
            Canvas(modifier = Modifier.fillMaxSize()) {
                rotate(degrees = 45f, pivot = Offset(200f,200f)) {
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