package com.example.datasaverexampleapp.compose.path_gender_selection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier

class GenderSelectionUsingPathExampleActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Gender selection using path example"
        setContent {
            GenderPicker(modifier = Modifier.fillMaxSize()) {
                Toast.makeText(this,"${if (it == Gender.Male) "Male" else "Female"} selected!",Toast.LENGTH_SHORT).show()
            }
        }
    }
}