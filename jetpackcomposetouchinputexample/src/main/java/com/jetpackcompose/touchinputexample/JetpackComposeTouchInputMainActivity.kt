package com.jetpackcompose.touchinputexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jetpackcompose.touchinputexample.ui.theme.ComposeDetectTouchInputActivityView

class JetpackComposeTouchInputMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Compose detect touch input example"
        setContent {
            ComposeDetectTouchInputActivityView()
        }
    }
}