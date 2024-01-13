@file:OptIn(ExperimentalFoundationApi::class)

package com.jetpackcompose.secretmodifier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.MagnifierStyle
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.magnifier
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class JetpackComposeSecretModifierMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
            ) {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {

                    composable("home") {

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Button(onClick = {
                                navController.navigate("marquee")
                            }) {
                                Text(text = "Text marquee example")
                            }

                            Button(onClick = {
                                navController.navigate("image_magnifier")
                            }) {
                                Text(text = "Image magnifier example")
                            }

                            Button(onClick = {
                                navController.navigate("canvas_box")
                            }) {
                                Text(text = "Draw shape without canvas in box")
                            }
                        }
                    }

                    composable("marquee") {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Text(
                                text = "This is a very long text. This is a very long text. This is a very long text. This is a very long text. This is a very long text....",
                                maxLines = 1,
                                modifier = Modifier.basicMarquee()
                            )
                        }
                    }

                    composable("image_magnifier") {

                        var offset by remember {
                            mutableStateOf(Offset.Zero)
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .pointerInput(true) {
                                    detectDragGestures { change, _ ->
                                        offset = change.position
                                    }
                                }
                                .magnifier(sourceCenter = {
                                    offset
                                }, magnifierCenter = {
                                    offset - Offset(x = 0f, y = 200f)
                                }, style = MagnifierStyle(
                                    size = DpSize(width = 100.dp, height = 100.dp),
                                    cornerRadius = 100.dp
                                )
                                )
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ferrari_f1),
                                contentDescription = null,
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.FillWidth
                            )
                        }
                    }

                    composable("canvas_box") {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .drawWithContent {
                                    drawContent()
                                    drawCircle(color = Color.Red,
                                        radius = 100f,
                                        center = Offset(x = 100f, y = 100f)
                                    )
                                }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ferrari_f1),
                                contentDescription = null,
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.FillWidth
                            )
                        }
                    }
                }
            }
        }
    }
}

