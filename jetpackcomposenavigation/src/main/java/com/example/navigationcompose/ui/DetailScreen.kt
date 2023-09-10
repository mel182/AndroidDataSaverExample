package com.example.navigationcompose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DetailScreen(navController: NavController, name:String?) {

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 50.dp)
            .background(Color.White))
    {
        Text(text = "Detail item with param: $name")
        Button(
            onClick = {
                navController.navigate(Screen.DetailScreen.withArgs("test123"))
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "To detail screen 123")
        }
    }
}