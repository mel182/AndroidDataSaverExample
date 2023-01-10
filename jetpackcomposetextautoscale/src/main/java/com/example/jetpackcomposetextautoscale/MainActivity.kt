package com.example.jetpackcomposetextautoscale

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposetextautoscale.component.AutoResizedText
import com.example.jetpackcomposetextautoscale.ui.theme.DataSaverExampleAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataSaverExampleAppTheme {

                val width:Dp = 250.dp

                Column(modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(text = "Text without auto scaling", modifier = Modifier.padding(all = 5.dp))
                    Box(modifier = Modifier
                        .width(width)
                        .background(color = Color.LightGray)
                    ) {
                        Text(
                            text = "Hello world",
                            style = MaterialTheme.typography.h2,
                            softWrap = false
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = "Text with auto scaling", modifier = Modifier.padding(all = 5.dp))

                    Box(modifier = Modifier
                        .width(width)
                        .background(color = Color.LightGray)
                    ) {
                        AutoResizedText(
                            text = "Hello world",
                            style = MaterialTheme.typography.h2,
                        )
                    }
                }
            }
        }
    }
}