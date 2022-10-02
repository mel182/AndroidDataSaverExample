package com.example.datasaverexampleapp.compose.path

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

class PathCapExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Path cap example"

        setContent {

            Column(modifier = Modifier.fillMaxSize()) {

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
            }
        }
    }
}