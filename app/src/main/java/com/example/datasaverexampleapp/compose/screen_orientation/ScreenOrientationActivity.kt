package com.example.datasaverexampleapp.compose.screen_orientation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import com.example.datasaverexampleapp.compose.screen_orientation.ui.ScreenOrientationView

class ScreenOrientationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScreenOrientationView()
        }
    }
}