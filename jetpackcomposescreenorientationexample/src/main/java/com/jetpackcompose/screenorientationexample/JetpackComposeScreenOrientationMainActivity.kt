package com.jetpackcompose.screenorientationexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.datasaverexampleapp.compose.screen_orientation.ui.ScreenOrientationView

class JetpackComposeScreenOrientationMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScreenOrientationView()
        }
    }
}