package com.example.sharedatabetweencomposeview.using_shared_viewmodel.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PersonalDetailsScreen(sharedState:Int, 
                          onNavigate: () -> Unit
) {
    Button(onClick = { onNavigate() }) {
        Text(text = "Click me")
    }
}