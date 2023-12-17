package com.jetpackcompose.weightscaleexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.datasaverexampleapp.compose.weight_scale.ui.WeightScaleExampleView

class JetpackComposeWeightScaleMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Weight scale example"

        setContent {
            WeightScaleExampleView()
        }
    }
}