package com.example.datasaverexampleapp.custom_view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityCustomViewBinding
import com.example.datasaverexampleapp.type_alias.Layout

class CustomViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view)

        DataBindingUtil.setContentView<ActivityCustomViewBinding>(
            this, Layout.activity_custom_view
        ).apply {
            Toast.makeText(this@CustomViewActivity, "Price: ${tvExample.price}", Toast.LENGTH_SHORT).show()
        }
    }
}