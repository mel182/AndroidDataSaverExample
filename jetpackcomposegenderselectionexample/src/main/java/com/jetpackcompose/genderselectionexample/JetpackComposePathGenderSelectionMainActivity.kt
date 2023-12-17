package com.jetpackcompose.genderselectionexample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier

class JetpackComposePathGenderSelectionMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Gender selection using path example"
        setContent {
            GenderPicker(modifier = Modifier.fillMaxSize()) {
                Toast.makeText(this,"${if (it == Gender.Male) "Male" else "Female"} selected!",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}