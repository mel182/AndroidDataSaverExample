package com.example.datasaverexampleapp.appbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.datasaverexampleapp.R

class AppBarActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_bar2)

        title = "App bar example 2"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}