package com.example.datasaverexampleapp.compose.clock_example

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import kotlinx.coroutines.delay

class ComposeClockExampleActivity : AppCompatActivity() {
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