package com.jetpackcompose.pathexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

class JetpackComposePathExampleMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Canvas Path example"

        setContent {

            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
            ) {

                //region Simple Rectangle
                Column(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                    Text(text = "Simple Rectangle example", fontWeight = FontWeight.Bold, modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 10.dp), textAlign = TextAlign.Center)
                    Canvas(modifier = Modifier.fillMaxWidth()) {

                        val path = Path().apply {
                            moveTo(100f,100f)
                            lineTo(100f,500f)
                            lineTo(500f,500f)
                            lineTo(500f,100f)
                            lineTo(100f,100f)
                        }

                        drawPath(
                            path = path,
                            color = Color.Red,
                            style = Stroke(width = 2.dp.toPx()) //remove this line if you want it to be filled
                        )
                    }
                    Divider(modifier = Modifier.fillMaxWidth().height(2.dp).background(color = Color.LightGray))
                }
                //endregion

                //region Path cap
                Column(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                    Text(text = "Path cap example", fontWeight = FontWeight.Bold, modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 10.dp), textAlign = TextAlign.Center)
                    Canvas(modifier = Modifier.fillMaxWidth()) {

                        val path = Path().apply {
                            moveTo(100f,100f)
                            lineTo(100f,500f)
                            lineTo(500f,500f)
                            cubicTo(800f,500f,800f,100f,500f,100f) // This is the cubic example
                            // The close() function must be removed otherwise the path cap will not work
                        }

                        drawPath(
                            path = path,
                            color = Color.Red,
                            style = Stroke(
                                width = 10.dp.toPx(), //remove this line if you want it to be filled
                                cap = StrokeCap.Round // This is the path cap round example
//                                cap = StrokeCap.Butt // This is the path cap butt example (default)
//                                cap = StrokeCap.Square // This is the path cap square example
                            )
                        )
                    }
                    Divider(modifier = Modifier.fillMaxWidth().height(2.dp).background(color = Color.LightGray))
                }
                //endregion

                //region Path stroke join
                Column(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                    Text(text = "Path stroke join example", fontWeight = FontWeight.Bold, modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 10.dp), textAlign = TextAlign.Center)
                    Canvas(modifier = Modifier.fillMaxWidth()) {

                        val path = Path().apply {
//                            moveTo(100f,100f)
                            moveTo(1000f,100f) // Use this when using the stroke join miter example
                            lineTo(100f,500f)
                            lineTo(500f,500f)
                            cubicTo(800f,500f,800f,100f,500f,100f)
                            // The close() function must be removed otherwise the path cap will not work
                        }

                        drawPath(
                            path = path,
                            color = Color.Red,
                            style = Stroke(
                                width = 10.dp.toPx(), //remove this line if you want it to be filled
                                cap = StrokeCap.Round,
//                                join =  StrokeJoin.Round // This is the stroke join round example
//                                join =  StrokeJoin.Bevel // This is the stroke join bevel example
                                join =  StrokeJoin.Miter, // This is the stroke join miter example
//                                miter = 0f // will cut edges
                                miter = 20f // for shaper edges
                            )
                        )
                    }
                    Divider(modifier = Modifier.fillMaxWidth().height(2.dp).background(color = Color.LightGray))
                }
                //endregion

                //region Quadratic bezier and cubic path
                Column(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                    Text(text = "Quadratic bezier and cubic path example", fontWeight = FontWeight.Bold, modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 10.dp), textAlign = TextAlign.Center)
                    Canvas(modifier = Modifier.fillMaxWidth()) {

                        val path = Path().apply {
                            moveTo(100f,100f)
                            lineTo(100f,500f)
                            lineTo(500f,500f)
//                            quadraticBezierTo(800f, 300f, 500f, 100f) // This is the quadratic bezier example
                            cubicTo(800f,500f,800f,100f,500f,100f) // This is the cubic example
                            close() // does the same job of closing
                            // the line with the initial point
                            // (e.g. in this case 'lineTo(100f,100f)')
                        }

                        drawPath(
                            path = path,
                            color = Color.Red,
                            style = Stroke(width = 2.dp.toPx()) //remove this line if you want it to be filled
                        )
                    }
                    Divider(modifier = Modifier.fillMaxWidth().height(2.dp).background(color = Color.LightGray))
                }
                //endregion

            }
        }
    }
}