package com.example.datasaverexampleapp.appbar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_app_bar.*
import kotlinx.android.synthetic.main.activity_app_bar1.*

class AppBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_bar)

        //-------------------------------------- Themes --------------------------------------------------------------- \\
        // Theme.AppCompat:                     Use when you have a dark background for your UI and a dark color primary.
        //                                      Text colors will be light to contrast with the dark background.
        // Theme.AppCompat.Light:               Use with a light background and primary color; it provides dark text
        // Theme.AppCompat.Light.DarkActionBar: Matches the light theme, but inverts the colors specifically for the app bar
        //-------------------------------------- Themes --------------------------------------------------------------- \\

        // Set action bar title
        supportActionBar?.title = "App bar Example"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        app_bar_activity_button?.setOnClickListener {
            val intent = Intent(this, AppBarActivity1::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        intent?.let {
            val data = it.getStringExtra("data")
            Log.i("TAG","Date value: ${data}")
            extra_intent_textView?.text = data
        }
    }


}