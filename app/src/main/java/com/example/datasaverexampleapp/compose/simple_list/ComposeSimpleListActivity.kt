package com.example.datasaverexampleapp.compose.simple_list

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.datasaverexampleapp.compose.simple_list.ui.ComposeSimpleLazyColumnList
import com.example.datasaverexampleapp.compose.simple_list.ui.ComposeSimpleListView

class ComposeSimpleListActivity : AppCompatActivity() {

    @SuppressLint("UnrememberedMutableState")
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
                        Text(text = "column example")
                    }
                    Button(onClick = {
                        viewState.value = 2
                    }) {
                        Text(text = "lazy column example")
                    }
                }
            }
        }
    }
}