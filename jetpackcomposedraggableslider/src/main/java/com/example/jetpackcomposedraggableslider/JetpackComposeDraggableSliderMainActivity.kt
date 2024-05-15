package com.example.jetpackcomposedraggableslider

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposedraggableslider.ui.theme.DataSaverExampleAppTheme

class JetpackComposeDraggableSliderMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DataSaverExampleAppTheme {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0xFF191C1E)),
                    verticalArrangement = Arrangement.Center
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        DraggableSlider(modifier = Modifier
                            .fillMaxWidth()
                            .align(alignment = Alignment.Center)
                            .fillMaxHeight(),
                            stroke = 40f,
                            onChange = {
                                println("on change: $it")
                            }
                        )
                    }
                }
            }
        }
    }
}