package com.example.datasaverexampleapp.compose.tablayout_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.example.datasaverexampleapp.compose.tablayout_example.ui.CustomTabText
import com.example.datasaverexampleapp.compose.tablayout_example.ui.MainContent

class ComposeTabLayoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Compose tab layout Example"
        setContent {
            MainContent()
        }
    }
}