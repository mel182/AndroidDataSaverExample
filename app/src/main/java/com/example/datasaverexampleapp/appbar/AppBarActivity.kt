package com.example.datasaverexampleapp.appbar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityAppBarBinding
import com.example.datasaverexampleapp.type_alias.Layout

class AppBarActivity : AppCompatActivity() {

    private var binding:ActivityAppBarBinding? = null

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

        binding = DataBindingUtil.setContentView<ActivityAppBarBinding>(
            this, Layout.activity_app_bar
        ).apply {
            appBarActivityButton.setOnClickListener {
                val intent = Intent(this@AppBarActivity, AppBarActivity1::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.apply {
            intent?.let {
                val data = it.getStringExtra("data")
                Log.i("TAG","Date value: ${data}")
                extraIntentTextView.text = data
            }
        }
    }
}