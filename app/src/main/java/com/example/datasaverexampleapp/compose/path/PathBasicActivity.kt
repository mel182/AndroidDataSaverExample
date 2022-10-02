package com.example.datasaverexampleapp.compose.path

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

class PathBasicActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Path basic example"

        setContent {

            Column(modifier = Modifier.fillMaxSize()) {

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
            }
        }
    }
}