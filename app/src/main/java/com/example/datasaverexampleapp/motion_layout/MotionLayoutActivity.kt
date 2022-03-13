package com.example.datasaverexampleapp.motion_layout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.type_alias.Layout

class MotionLayoutActivity : AppCompatActivity(Layout.activity_motion_layout) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Motion layout Example"
    }
}