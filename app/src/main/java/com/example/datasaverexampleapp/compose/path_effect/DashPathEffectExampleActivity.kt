package com.example.datasaverexampleapp.compose.path_effect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

class DashPathEffectExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Dash path effect example"
        setContent {
            // This example show a dash path effect
            Canvas(modifier = Modifier.fillMaxSize()) {

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
    }
}