package com.example.datasaverexampleapp.appbar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_app_bar1.*

class AppBarActivity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_bar1)

        title = "App bar example 1"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        app_bar_activity_2_button?.setOnClickListener {
            val intent = Intent(this, AppBarActivity2::class.java)
            startActivity(intent)
        }
    }

    // If you want pass data back to the parent activity override the 'getSupportParentActivityIntent()' function
    override fun getSupportParentActivityIntent(): Intent?
    {
        val intent = super.getSupportParentActivityIntent()
        intent?.putExtra("data","Extra string")
        return intent
    }
}