package com.jetpackcompose.imageediting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class JetpackComposeImageEditingMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Image editing example"
        setContent {
            ImageEditing()
        }
    }
}