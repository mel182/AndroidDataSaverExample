package com.jetpackcompose.pathoperation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

class JetpackComposePathOperationMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Path operations example"

        setContent {

            Canvas(modifier = Modifier.fillMaxSize()) {

                val squareWithoutOperations = Path().apply {
                    addRect(Rect(offset = Offset(200f,200f), size = Size(200f,200f)))
                }

                val circle = Path().apply {
                    addOval(Rect(center = Offset(200f,200f), radius = 100f))
                }

                val pathWitOperations = Path().apply {
                    op(path1 = squareWithoutOperations,
                        path2 = circle,
//                            operation = PathOperation.Difference // This is the path operations difference
//                            operation = PathOperation.ReverseDifference
//                            operation = PathOperation.Union // This is the path operations union
//                            operation = PathOperation.Intersect // This is the path operations intersect
                        operation = PathOperation.Xor // This is the path operations Xor
                    )
                }

                drawPath(
                    path = squareWithoutOperations,
                    color = Color.Red,
                    style = Stroke(width = 2.dp.toPx())
                )

                drawPath(
                    path = circle,
                    color = Color.Blue,
                    style = Stroke(width = 2.dp.toPx())
                )

                drawPath(
                    path = pathWitOperations,
                    color = Color.Green
                )
            }
        }
    }
}