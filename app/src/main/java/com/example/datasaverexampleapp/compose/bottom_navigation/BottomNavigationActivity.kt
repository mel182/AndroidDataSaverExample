package com.example.datasaverexampleapp.compose.bottom_navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.example.datasaverexampleapp.compose.bottom_navigation.ui.MainScreen
import com.example.datasaverexampleapp.compose.row_and_column_example.ui.CustomText

class BottomNavigationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Bottom navigation Example"
        setContent {
            MainScreen()
        }
    }
}