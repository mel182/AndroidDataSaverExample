package com.example.touchpointdetectionexample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.example.touchpointdetectionexample.ui.theme.DataSaverExampleAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TouchPointDetectionMainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataSaverExampleAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {

                    val pointer_count = remember { mutableStateOf(0) }
                    val changeBackgroundColor = remember { mutableStateOf(false) }
                    val isTouchingScreen = remember { mutableStateOf(false) }

                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(color = if (changeBackgroundColor.value) Color.Blue else Color.LightGray)
                        .pointerInput(Unit) {

                            awaitEachGesture {

                                awaitFirstDown()
                                Log.i("TAG55","Action down")
                                isTouchingScreen.value = true

                                do {
                                    val event = awaitPointerEvent()

                                    CoroutineScope(Dispatchers.Main).launch {
                                        delay(600)
                                        val pointerCount = event.changes.size

                                        //(pointerCount == 0 || pointerCount != pointer_count.value)
                                        if (isTouchingScreen.value && pointerCount == 4) {
                                            Log.i("TAG55","pointer count: $pointerCount")
                                            ///pointer_count.value = pointerCount
                                            changeBackgroundColor.value = true
                                        }
                                    }

                                } while (event.changes.any { it.pressed })

                                Log.i("TAG55","Action up!")
                                isTouchingScreen.value = false
                                changeBackgroundColor.value = false

                            }
                        }
                    )
                }
            }
        }
    }
}
