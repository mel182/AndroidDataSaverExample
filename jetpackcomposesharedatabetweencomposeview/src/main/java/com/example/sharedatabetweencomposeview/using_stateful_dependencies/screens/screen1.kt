package com.example.sharedatabetweencomposeview.using_stateful_dependencies.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NavScreen1(sharedState:Int, onNavigate: () -> Unit) {

    Column(modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Screen 1 with state count: $sharedState",
            color = Color.Black,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 10.dp),
            textAlign = TextAlign.Center
        )

        Button(onClick = { onNavigate() }) {
            Text(text = "Go to screen 2")
        }
    }

}