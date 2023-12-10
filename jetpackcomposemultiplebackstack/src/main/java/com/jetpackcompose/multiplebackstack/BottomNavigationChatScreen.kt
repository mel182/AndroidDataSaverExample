package com.jetpackcompose.multiplebackstack

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun ChatNavHost(padding: PaddingValues) {
    val chatNavController = rememberNavController()
    NavHost(navController = chatNavController, startDestination = "chat1") {
        for (i in 1..10) {
            composable("chat$i") {
                GenericScreen(
                    text = "Chat $i",
                    padding = padding,
                    onNextClick = {
                        if (i < 10) {
                            chatNavController.navigate("chat${i + 1}")
                        }
                    }
                )
            }
        }
    }
}