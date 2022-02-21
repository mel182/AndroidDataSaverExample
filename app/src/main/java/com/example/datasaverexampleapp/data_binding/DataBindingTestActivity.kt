@file:Suppress("UNNECESSARY_SAFE_CALL")

package com.example.datasaverexampleapp.data_binding

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityDataBindingTestBinding

class DataBindingTestActivity : AppCompatActivity(R.layout.activity_data_binding_test) {

    var binding : ActivityDataBindingTestBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_binding_test)

        val mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = DataBindingUtil.setContentView<ActivityDataBindingTestBinding>(
            this, R.layout.activity_data_binding_test
        ).apply {
            lifecycleOwner = this@DataBindingTestActivity
            viewmodel = mainViewModel

            dataBindingActivityMainView?.setOnClickListener {
                Toast.makeText(this@DataBindingTestActivity, "View clicked!", Toast.LENGTH_SHORT).show()
            }
        }

        mainViewModel.editTextContent.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }
}