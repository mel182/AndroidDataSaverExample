package com.example.datasaverexampleapp.custom_view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_custom_view.*

class CustomViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view)
        Toast.makeText(this, "Price: ${tv_example?.price}", Toast.LENGTH_SHORT).show()
    }
}