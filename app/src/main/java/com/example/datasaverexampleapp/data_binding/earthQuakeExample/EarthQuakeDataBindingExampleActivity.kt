package com.example.datasaverexampleapp.data_binding.earthQuakeExample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_earth_quake_data_binding_example.*
import kotlinx.coroutines.*

class EarthQuakeDataBindingExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earth_quake_data_binding_example)

        val customAdapter = EarthQuakeDataBindingExampleAdapter()

        list?.apply {
            layoutManager = LinearLayoutManager(this@EarthQuakeDataBindingExampleActivity)
            adapter = customAdapter
        }

        val earthQuakeViewModel = ViewModelProviders.of(this).get(EarthQuakeViewModel::class.java)
        earthQuakeViewModel.retrieveData().observe(this, { response ->
            customAdapter.loadData(response)
        })

        GlobalScope.launch {
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
    }
}