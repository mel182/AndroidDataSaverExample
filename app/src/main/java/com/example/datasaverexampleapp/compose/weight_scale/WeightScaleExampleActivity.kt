package com.example.datasaverexampleapp.compose.weight_scale

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.datasaverexampleapp.compose.weight_scale.ui.WeightScaleExampleView

class WeightScaleExampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Weight scale example"

        setContent {
            WeightScaleExampleView()
        }
    }
}