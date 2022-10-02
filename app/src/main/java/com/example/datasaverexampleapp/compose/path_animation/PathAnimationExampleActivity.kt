package com.example.datasaverexampleapp.compose.path_animation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

class PathAnimationExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Path animation example"

        setContent {

            var animationPath = remember {
                mutableStateOf(false)
            }

            if (animationPath.value) {
                AnimatePath {
                    animationPath.value = false
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(enabled = !animationPath.value,
                    onClick = {
                        if (!animationPath.value) {
                            animationPath.value = true
                        }
                    }) {
                    Text(text = "Test animation")
                }

                if (animationPath.value) {
                    Text(text = "Animating...")
                }
            }
        }
    }
}

@Composable
fun AnimatePath(onCompletion:() -> Unit) {

    val pathPortion = remember {
        Animatable(initialValue = 0f)
    }

    val path = Path().apply {
        moveTo(100f,100f)
        quadraticBezierTo(400f,400f,100f,400f)
    }

    val outPath = Path()
    PathMeasure().apply {
        setPath(path = path, forceClosed = false)
        getSegment(
            startDistance = 0f,
            stopDistance = pathPortion.value * length,
            destination = outPath
        )
    }

    Canvas(modifier = Modifier.fillMaxWidth()) {
        drawPath(
            path = outPath,
            color = Color.Red,
            style = Stroke(width = 5.dp.toPx())
        )
    }

    LaunchedEffect(key1 = true) {
        pathPortion.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 5000
            )
        )
        delay(5000)
        onCompletion()
    }
}
