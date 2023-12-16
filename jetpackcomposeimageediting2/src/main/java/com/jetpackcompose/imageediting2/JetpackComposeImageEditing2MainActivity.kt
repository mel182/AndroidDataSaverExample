package com.jetpackcompose.imageediting2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class JetpackComposeImageEditing2MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Image Editing Exercise"
        setContent {
            ImageEditingExerciseUI()
        }
    }
}