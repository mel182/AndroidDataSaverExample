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
import androidx.compose.ui.graphics.drawscope.rotate

class RotateRectangleWithAnimationExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Rotate rectangle with animation example"

        setContent {

            // This example shows how to rotate an rectangle 45 degrees with an animation duration of 3 sec.
            val animateDegrees = remember {
                Animatable(initialValue = 0f)
            }

            LaunchedEffect(key1 = true) {
                animateDegrees.animateTo(
                    targetValue = 45f,
                    animationSpec = tween(
                        durationMillis = 3000
                    )
                )
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                rotate(degrees = animateDegrees.value, pivot = Offset(200f,200f)) {
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