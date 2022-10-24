package com.example.datasaverexampleapp.compose.image_editing_exercise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent

class ImageEditingExerciseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Image Editing Exercise"
        setContent {
            ImageEditingExerciseUI()
        }
    }
}