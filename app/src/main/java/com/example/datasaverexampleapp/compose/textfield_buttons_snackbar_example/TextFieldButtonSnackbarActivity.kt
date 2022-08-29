package com.example.datasaverexampleapp.compose.textfield_buttons_snackbar_example

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.datasaverexampleapp.compose.textfield_buttons_snackbar_example.ui.TextFieldButtonAndSnackBarActivityView

class TextFieldButtonSnackbarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Textfield Button and Snackbar compose example"
        setContent {
            TextFieldButtonAndSnackBarActivityView()
        }
    }
}