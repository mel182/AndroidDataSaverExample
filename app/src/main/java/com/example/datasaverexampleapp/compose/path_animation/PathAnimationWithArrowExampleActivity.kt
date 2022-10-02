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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.atan2

class PathAnimationWithArrowExampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Path animation with arrow example"

        setContent {

            var animationPath = remember {
                mutableStateOf(false)
            }

            if (animationPath.value) {
                AnimatePathWithArrow {
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
fun AnimatePathWithArrow(onCompletion:() -> Unit) {

    val pathPortion = remember {
        Animatable(initialValue = 0f)
    }

    val path = Path().apply {
        moveTo(100f,100f)
        quadraticBezierTo(400f,400f,100f,400f)
    }

    val outPath = android.graphics.Path()
    val pos = FloatArray(2)
    val tan = FloatArray(2)
    android.graphics.PathMeasure().apply {
        setPath(path.asAndroidPath(), false)
        val distance = pathPortion.value * length
        getSegment(0f,distance,outPath,true)
        // The 'getPostTan' function get position and tan values of the given path
        getPosTan(distance, pos, tan)
    }

    Canvas(modifier = Modifier.fillMaxWidth()) {
        drawPath(
            path = outPath.asComposePath(),
            color = Color.Red,
            style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
        )

        val x = pos[0]
        val y = pos[1]
        val degrees = -atan2(tan[0], tan[1]) * (180f / PI.toFloat()) - 180f
        rotate(degrees = degrees, pivot = Offset(x, y)) {
            drawPath(
                path = Path().apply {
                    moveTo(x, y - 30f)
                    lineTo(x - 30f, y + 60f)
                    lineTo(x + 30f, y + 60f)
                    close()
                },
                color = Color.Red
            )
        }
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