package com.example.datasaverexampleapp.compose.screen_orientation.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.sp

@Composable
fun ScreenOrientationView() {

    // Fetching current app configuration
    val configuration = LocalConfiguration.current

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        when(configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> Text(text = "Landscape", fontSize = 20.sp)
            else -> Text(text = "Portrait", fontSize = 20.sp)
        }
    }
}