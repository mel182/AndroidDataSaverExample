package com.example.datasaverexampleapp.compose.navigation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.datasaverexampleapp.compose.navigation.NavigationHostScreen

@Composable
fun MainScreen(navController: NavController) {

    var text by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 50.dp)
    ) {
        TextField(value = text, onValueChange = {
            text = it
        }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            if (text.isNotBlank()) {
                navController.navigate(NavigationHostScreen.DetailScreen.withArgs(text))
            }
        }, enabled = text.isNotBlank(), modifier = Modifier.align(Alignment.End)) {
            Text(text = "To detail screen")
        }
    }
}