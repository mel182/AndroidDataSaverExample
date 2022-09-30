package com.example.datasaverexampleapp.compose.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent

class ComposeNavigationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Compose Navigation activity"
        setContent {
            Navigation()
        }
    }
}