package com.jetpackcompose.pathtransformationandclipping

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jetpackcompose.pathtransformationandclipping.ui.theme.DataSaverExampleAppTheme

class JetpackComposePathTransformationAndClippingMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LazyColumn(modifier = Modifier
                .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                content = {

                    item {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                        ) {
                            Text(text = "Clipping example", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Color.Black, fontSize = 16.sp)
                            Divider(modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(color = Color.LightGray))

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

                    item {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                        ) {
                            Text(text = "Combine translate and rotate example", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Color.Black, fontSize = 16.sp)
                            Divider(modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(color = Color.LightGray))

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

                    item {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                        ) {
                            Text(text = "Combine translate and rotate with animation example", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Color.Black, fontSize = 16.sp)
                            Divider(modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(color = Color.LightGray))

                            val translateValue = remember {
                                Animatable(initialValue = 0f)
                            }

                            val animateDegrees = remember {
                                Animatable(initialValue = 0f)
                            }

                            LaunchedEffect(key1 = true) {
                                translateValue.animateTo(
                                    targetValue = 300f,
                                    animationSpec = tween(
                                        durationMillis = 3000
                                    )
                                )
                                animateDegrees.animateTo(
                                    targetValue = 45f,
                                    animationSpec = tween(
                                        durationMillis = 3000
                                    )
                                )
                            }

                            Canvas(modifier = Modifier.fillMaxSize()) {

                                translate(left = translateValue.value, top = translateValue.value) {
                                    rotate(animateDegrees.value, pivot = Offset(100f,100f)) {
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

                    item {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                        ) {
                            Text(text = "Path rotate rectangle example", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Color.Black, fontSize = 16.sp)
                            Divider(modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(color = Color.LightGray))
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

                    item {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                        ) {
                            Text(text = "Rotate rectangle with animation example", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Color.Black, fontSize = 16.sp)
                            Divider(modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(color = Color.LightGray))

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

                    item {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                        ) {
                            Text(text = "Scale shape example", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Color.Black, fontSize = 16.sp)
                            Divider(modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(color = Color.LightGray))

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

                    item {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                        ) {
                            Text(text = "Scale shape with animation example", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Color.Black, fontSize = 16.sp)
                            Divider(modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(color = Color.LightGray))

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

                    item {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                        ) {
                            Text(text = "Translate rectangle example", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Color.Black, fontSize = 16.sp)
                            Divider(modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(color = Color.LightGray))

                            // This example shows how to translate rectangle to coordinate 300,300
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                translate(left = 300f, top = 300f) {
                                    drawRect(
                                        color = Color.Red,
                                        topLeft = Offset(x =100f,y = 100f),
                                        size = Size(width = 200f, height = 200f)
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                        ) {
                            Text(text = "Translate rectangle with animation example", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Color.Black, fontSize = 16.sp)
                            Divider(modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(color = Color.LightGray))

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
            )
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DataSaverExampleAppTheme {
        Greeting("Android")
    }
}