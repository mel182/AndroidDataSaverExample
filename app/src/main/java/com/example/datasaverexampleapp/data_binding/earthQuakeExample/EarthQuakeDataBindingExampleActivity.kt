package com.example.datasaverexampleapp.data_binding.earthQuakeExample

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_earth_quake_data_binding_example.*
import kotlinx.coroutines.*

class EarthQuakeDataBindingExampleActivity : AppCompatActivity() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earth_quake_data_binding_example)

        val customAdapter = EarthQuakeDataBindingExampleAdapter()

        list?.apply {
            layoutManager = LinearLayoutManager(this@EarthQuakeDataBindingExampleActivity)
            adapter = customAdapter
        }

        val earthQuakeViewModel = ViewModelProvider(this).get(EarthQuakeViewModel::class.java)
        earthQuakeViewModel.retrieveData().observe(this) { response ->
            customAdapter.loadData(response)
        }

        CoroutineScope(Dispatchers.Main).launch {
            coroutineScope {

                delay(2000)
                launch(Dispatchers.Main)
                {
                    EarthQuakeRepository.loadData(1)
                }
                delay(2000)
                launch(Dispatchers.Main)
                {
                    EarthQuakeRepository.loadData(2)
                }
            }
        }

        list?.setOnTouchListener { _, event ->
            
            if (event.action == MotionEvent.ACTION_DOWN)
            {
                Log.i("TAG","Tap registered!")
                Toast.makeText(this, "Tap", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }
}