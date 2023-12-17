package com.jetpackcompose.navigationexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class JetpackComposeNavigationExampleMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Compose Navigation activity"
        setContent {
            Navigation()
        }
    }
}