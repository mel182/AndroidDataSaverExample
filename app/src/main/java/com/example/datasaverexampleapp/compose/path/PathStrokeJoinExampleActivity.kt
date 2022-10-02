package com.example.datasaverexampleapp.compose.path

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

class PathStrokeJoinExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Path stroke join example"

        setContent {

            Column(modifier = Modifier.fillMaxSize()) {

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
            }
        }
    }
}