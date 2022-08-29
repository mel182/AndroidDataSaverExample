package com.example.datasaverexampleapp.compose.bottom_sheet

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity

class BottomSheetComposeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Bottom sheet compose example"
        setContent {
            BottomSheetView()
        }
    }
}