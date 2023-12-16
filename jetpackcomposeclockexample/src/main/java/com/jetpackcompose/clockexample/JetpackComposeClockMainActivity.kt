package com.jetpackcompose.clockexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

class JetpackComposeClockMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Compose clock example"
        setContent {

            val milliseconds = remember {
                System.currentTimeMillis()
            }
            var seconds by remember {
                mutableStateOf((milliseconds/ 1000f) % 60f)
            }
            var minutes by remember {
                mutableStateOf(((milliseconds / 1000f) / 60) % 60f)
            }
            var hours by remember {
                mutableStateOf((milliseconds / 1000f) / 3600f + 2f)
            }
            LaunchedEffect(key1 = seconds) {
                delay(1000L)
                minutes += 1f / 60f
                hours += 1f / (60f * 12f)
                seconds += 1f
            }

            ComposeClockActivityView(seconds = seconds, minutes = minutes, hours = hours)
        }
    }
}