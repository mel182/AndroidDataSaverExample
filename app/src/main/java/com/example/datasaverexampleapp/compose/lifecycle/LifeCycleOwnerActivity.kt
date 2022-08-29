package com.example.datasaverexampleapp.compose.lifecycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.example.datasaverexampleapp.compose.lifecycle.ui.LifeCycleOwnerActivityView

/**
 * This example demonstrates the activity life cycle of an activity in jetpack compose
 */
class LifeCycleOwnerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LifeCycleOwnerActivityView()
        }
    }
}