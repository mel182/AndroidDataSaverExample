package com.jetpackcompose.patheffect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

class JetpackComposePathEffectMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // This example show a dash path effect
            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .height(460.dp)
                ) {
                    Text(text = "Dash path effect", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Color.Black)
                    Divider(modifier = Modifier.fillMaxWidth().height(2.dp).background(color = Color.LightGray))
                    Canvas(modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)) {

                        val path = Path().apply {
                            moveTo(100f,100f)
                            cubicTo(100f,300f,600f,700f,600f,1100f)
                        }
                        drawPath(
                            path = path,
                            color = Color.Red,
                            style = Stroke(
                                width = 5.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(
                                    intervals = floatArrayOf(50f, 30f)
                                )
                            )
                        )
                    }
                }

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .height(460.dp)
                ) {
                    Text(text = "Dash path effect with animation", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Color.Black)
                    Divider(modifier = Modifier.fillMaxWidth().height(2.dp).background(color = Color.LightGray))

                    val infiniteTransition = rememberInfiniteTransition()
                    val phase by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 10000f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(durationMillis = 60000, easing = LinearEasing)
                        )
                    )

                    Canvas(modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)) {

                        val path2 = Path().apply {
                            moveTo(100f,100f)
                            cubicTo(100f,300f,600f,700f,600f,1100f)
                        }

                        drawPath(
                            path = path2,
                            color = Color.Red,
                            style = Stroke(
                                width = 5.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(
                                    intervals = floatArrayOf(50f, 30f),
                                    phase = phase
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}