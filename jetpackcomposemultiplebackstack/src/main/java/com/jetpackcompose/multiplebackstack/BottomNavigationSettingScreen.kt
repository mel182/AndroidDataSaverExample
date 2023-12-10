package com.jetpackcompose.multiplebackstack

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun SettingsNavHost(padding: PaddingValues) {
    val settingsNavController = rememberNavController()
    NavHost(navController = settingsNavController, startDestination = "settings1") {
        for (i in 1..10) {
            composable("settings$i") {
                GenericScreen(
                    text = "Settings $i",
                    padding = padding,
                    onNextClick = {
                        if (i < 10) {
                            settingsNavController.navigate("settings${i + 1}")
                        }
                    }
                )
            }
        }
    }
}