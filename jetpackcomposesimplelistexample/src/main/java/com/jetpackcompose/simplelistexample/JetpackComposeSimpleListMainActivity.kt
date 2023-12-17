package com.jetpackcompose.simplelistexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.datasaverexampleapp.compose.simple_list.ui.ComposeSimpleLazyColumnList
import com.example.datasaverexampleapp.compose.simple_list.ui.ComposeSimpleListView

class JetpackComposeSimpleListMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewState = remember {
                mutableStateOf(-1)
            }

            if (viewState.value == 1) {
                ComposeSimpleListView()
            } else if (viewState.value == 2) {
                ComposeSimpleLazyColumnList()
            } else {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = {
                        viewState.value = 1
                    }) {
                        androidx.compose.material.Text(text = "column example")
                    }
                    Button(onClick = {
                        viewState.value = 2
                    }) {
                        androidx.compose.material.Text(text = "lazy column example")
                    }
                }
            }
        }
    }
}