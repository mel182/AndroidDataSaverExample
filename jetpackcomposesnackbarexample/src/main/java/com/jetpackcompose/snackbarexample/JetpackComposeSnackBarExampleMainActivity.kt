package com.jetpackcompose.snackbarexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.datasaverexampleapp.compose.textfield_buttons_snackbar_example.ui.TextFieldButtonAndSnackBarActivityView

class JetpackComposeSnackBarExampleMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Textfield Button and Snackbar compose example"
        setContent {
            TextFieldButtonAndSnackBarActivityView()
        }
    }
}