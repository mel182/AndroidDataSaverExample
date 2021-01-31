package com.example.datasaverexampleapp.appbar.scrolling_techniques

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_app_bar_advance_scrolling.*
import kotlinx.android.synthetic.main.activity_app_bar_collapsing_toolbar_example.*
import kotlinx.android.synthetic.main.activity_app_bar_collapsing_toolbar_example.data_list

class AppBarCollapsingToolbarWithImageExampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_bar_collapsing_toolbar_with_image_example)

        collapse_toolbar?.apply {
            setTitleTextColor(Color.WHITE)
            setSupportActionBar(this)
            supportActionBar?.title = "App bar collapsing toolbar Example"
        }

        val data = arrayListOf(
            "value 1", "value 2", "value 3",
            "value 4", "value 5", "value 6",
            "value 7", "value 8", "value 9",
            "value 10", "value 11", "value 12",
            "value 13", "value 14", "value 15",
            "value 16", "value 17", "value 18",
            "value 19", "value 20", "value 21",
            "value 22", "value 23", "value 24",
            "value 25", "value 26", "value 27",
            "value 28", "value 29", "value 30",
            "value 31", "value 32", "value 33")

        data_list?.apply {
            layoutManager = LinearLayoutManager(this@AppBarCollapsingToolbarWithImageExampleActivity)
            adapter = DataAdapter(data)
        }

    }
}