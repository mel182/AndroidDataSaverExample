package com.example.datasaverexampleapp.compose.path_transformations_and_clipping

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale

class ScaleShapeWithAnimationExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Scale shape with animation example"
        setContent {
            // In this example we will scale shape down to half of it
            // original size with an animation of 3 sec

            val scaleValue = remember {
                Animatable(initialValue = 1.0f)
            }

            LaunchedEffect(key1 = true) {
                scaleValue.animateTo(
                    targetValue = 0.5f,
                    animationSpec = tween(
                        durationMillis = 3000
                    )
                )
            }

            Canvas(modifier = Modifier.fillMaxSize()) {

                scale(scale = scaleValue.value, pivot = Offset(x = 200f, y = 200f)) {
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