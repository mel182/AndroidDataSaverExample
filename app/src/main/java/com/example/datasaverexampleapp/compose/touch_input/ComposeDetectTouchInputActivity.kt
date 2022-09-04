package com.example.datasaverexampleapp.compose.touch_input

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.example.datasaverexampleapp.compose.touch_input.ui.ComposeDetectTouchInputActivityView

class ComposeDetectTouchInputActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Compose detect touch input example"
        setContent {
            ComposeDetectTouchInputActivityView()
        }
    }
}