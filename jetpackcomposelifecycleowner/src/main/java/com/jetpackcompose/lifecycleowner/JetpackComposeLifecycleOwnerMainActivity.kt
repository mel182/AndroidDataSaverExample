package com.jetpackcompose.lifecycleowner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.datasaverexampleapp.compose.lifecycle.ui.LifeCycleOwnerActivityView

class JetpackComposeLifecycleOwnerMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LifeCycleOwnerActivityView()
        }
    }
}