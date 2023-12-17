package com.jetpackcompose.navigationexample.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jetpackcompose.navigationexample.NavigationHostScreen

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