package com.jetpackcompose.tablayoutexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jetpackcompose.tablayoutexample.ui.MainContent

class JetpackComposeTabLayoutExampleMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Compose tab layout Example"
        setContent {
            MainContent()
        }
    }
}