package com.example.datasaverexampleapp.compose

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.compose.simple_example.SimpleComposeActivity
import com.example.datasaverexampleapp.data_binding.earthQuakeExample.EarthQuakeDataBindingExampleActivity
import com.example.datasaverexampleapp.databinding.ActivityComposeBinding
import com.example.datasaverexampleapp.databinding.ActivityMainBinding
import com.example.datasaverexampleapp.type_alias.Layout

class ComposeActivity : AppCompatActivity() {

    private var binding: ActivityComposeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(Layout.activity_compose)
        title = "Compose example"
        binding = DataBindingUtil.setContentView(this, Layout.activity_compose)

        binding?.apply {

            simpleExample.setOnClickListener {
                val intent = Intent(this@ComposeActivity, SimpleComposeActivity::class.java)
                startActivity(intent)
            }
        }
    }
}