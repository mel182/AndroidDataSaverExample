package com.jetpackcompose.customshape

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jetpackcompose.customshape.ui.theme.DataSaverExampleAppTheme

/**
 * This example shows how to create custom shapes in jetpack compose
 */
class JetpackComposeCustomShapeMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataSaverExampleAppTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White)
                ) {

                    Box(modifier = Modifier
                        .size(width = 200.dp, height = 100.dp)
                        .clip(SpeechBubbleShape())
                        .background(color = Color.Red)
                    ) {
                        Text(
                            text = "Hello World!",
                            color = Color.Black,
                            modifier = Modifier.offset(x = 20.dp)
                        )
                    }
                }
            }
        }
    }
}
