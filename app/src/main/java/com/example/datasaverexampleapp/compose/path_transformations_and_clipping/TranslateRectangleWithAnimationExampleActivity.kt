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
import androidx.compose.ui.graphics.drawscope.translate

class TranslateRectangleWithAnimationExampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Translate rectangle with animation example"

        setContent {

           // This example shows how to translate rectangle to coordinate 300,300 with animation
            val translationValue = remember {
                Animatable(initialValue = 0f)
            }

            LaunchedEffect(key1 = true) {
                translationValue.animateTo(
                    targetValue = 300f,
                    animationSpec = tween(
                        durationMillis = 3000
                    )
                )
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                translate(left = translationValue.value, top = translationValue.value) {
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