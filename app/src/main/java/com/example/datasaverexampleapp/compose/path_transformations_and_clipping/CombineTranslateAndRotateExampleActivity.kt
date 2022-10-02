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
import androidx.compose.ui.graphics.drawscope.translate

class CombineTranslateAndRotateExampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Combine translate and rotate example"
        setContent {
            Canvas(modifier = Modifier.fillMaxSize()) {
                translate(left = 300f, top = 300f) {
                    rotate(45f, pivot = Offset(100f,100f)) {
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
}