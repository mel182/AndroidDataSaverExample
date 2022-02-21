package com.example.datasaverexampleapp.appbar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityAppBar1Binding
import com.example.datasaverexampleapp.type_alias.Layout

class AppBarActivity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_bar1)

        title = "App bar example 1"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        DataBindingUtil.setContentView<ActivityAppBar1Binding>(
            this, Layout.activity_app_bar1
        ).apply {
            appBarActivity2Button.setOnClickListener {
                val intent = Intent(this@AppBarActivity1, AppBarActivity2::class.java)
                startActivity(intent)
            }
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