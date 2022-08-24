package com.example.datasaverexampleapp.compose

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.compose.row_and_column_example.RowAndColumnComposeActivity
import com.example.datasaverexampleapp.databinding.ActivityComposeBinding
import com.example.datasaverexampleapp.type_alias.Layout

class ComposeActivity : AppCompatActivity() {

    private var binding: ActivityComposeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(Layout.activity_compose)
        title = "Compose example"
        binding = DataBindingUtil.setContentView(this, Layout.activity_compose)

        binding?.apply {

            rowColumnExample.setOnClickListener {
                val intent = Intent(this@ComposeActivity, RowAndColumnComposeActivity::class.java)
                startActivity(intent)
            }
        }
    }
}