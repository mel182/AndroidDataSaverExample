package com.example.splashdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.splashdemo.ui.theme.DataSaverExampleAppTheme

class MainActivity : ComponentActivity()
{
    // Animated icon size max. 58 x 58
    // Animated background max. 108 x 108
    // More info about the designing a animated icon read this article:
    // https://proandroiddev.com/sketch-animated-vector-drawable-%EF%B8%8F-41fb63465b61

    private val demoViewModel: DemoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition{
                demoViewModel.isLoading.value
            }
        }

        setContent {
            DataSaverExampleAppTheme {
                // A surface container using the 'background' color from the theme
                Box(modifier = Modifier.fillMaxSize().background(color = Color.White),
                    contentAlignment = Alignment.Center) {
                    Text(text = "Main screen")
                }
            }
        }
    }
}