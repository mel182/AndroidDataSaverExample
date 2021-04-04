package com.example.datasaverexampleapp.data_binding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityDataBindingTestBinding
import kotlinx.android.synthetic.main.activity_data_binding_test.*

class DataBindingTestActivity : AppCompatActivity(R.layout.activity_data_binding_test) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_binding_test)

        val mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        DataBindingUtil.setContentView<ActivityDataBindingTestBinding>(
            this, R.layout.activity_data_binding_test
        ).apply {
            lifecycleOwner = this@DataBindingTestActivity
            viewmodel = mainViewModel
        }

        mainViewModel.editTextContent.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        data_binding_activity_main_view?.setOnClickListener {
            Toast.makeText(this, "View clicked!", Toast.LENGTH_SHORT).show()
        }


    }
}