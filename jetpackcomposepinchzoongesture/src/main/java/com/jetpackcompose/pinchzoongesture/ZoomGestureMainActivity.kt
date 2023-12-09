package com.jetpackcompose.pinchzoongesture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.jetpackcompose.pinchzoongesture.ui.theme.DataSaverExampleAppTheme

class ZoomGestureMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataSaverExampleAppTheme {
                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Greeting("Android")
//                }

                var scale by remember {
                    mutableStateOf(1f)
                }
                var rotation by remember {
                    mutableStateOf(1f)
                }
                var offset by remember {
                    mutableStateOf(Offset.Zero)
                }

                BoxWithConstraints(modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1920f / 1280f)
                ) {
                    val state = rememberTransformableState { zoomChange, panChange, rotationChange ->
                                // scale * zoomChange determine the zoom level and coerceIn determine the boundaries, like in this case
                                // minimum zoom-out is 1 and maximal zoom-in is 5.
                        scale = (scale * zoomChange).coerceIn(1f, 5f)

                        rotation += rotationChange

                        val extraWidth = (scale - 1) * constraints.maxWidth
                        val extraHeight = (scale - 1) * constraints.maxHeight

                        val maxX = extraWidth / 2
                        val maxY = extraHeight / 2

                        offset = Offset(
                            x = (offset.x + scale + panChange.x).coerceIn(-maxX, maxX),
                            y = (offset.y + scale + panChange.y).coerceIn(-maxY, maxY)
                        )
                    }

                    Image(painter = painterResource(id = R.drawable.ferrari_2023),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                rotationZ = rotation
                                translationX = offset.x
                                translationY = offset.y
                            }.transformable(state)
                    )
                }
            }
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