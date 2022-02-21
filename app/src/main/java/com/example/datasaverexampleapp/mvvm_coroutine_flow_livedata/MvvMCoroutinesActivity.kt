package com.example.datasaverexampleapp.mvvm_coroutine_flow_livedata

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityMvvMCoroutinesBinding
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MvvMCoroutinesActivity : AppCompatActivity() {

    private var binding: ActivityMvvMCoroutinesBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvv_m_coroutines)
        binding = DataBindingUtil.setContentView<ActivityMvvMCoroutinesBinding>(
            this, Layout.activity_mvv_m_coroutines
        )
//        makeNormalCall()
        makeCallWithCoroutine()
    }

    private fun makeNormalCall()
    {
        DemoViewModel().getAsyncDemo(1).observe(this, Observer { response ->

            binding?.apply {

                if (!response.model1.requestFailed()) {
                    Toast.makeText(this@MvvMCoroutinesActivity,
                        "request succeed model 1!",
                        Toast.LENGTH_SHORT
                    ).show()

                    userId.text = response.model1.userID.toString()
                    id.text = response.model1.id.toString()
                    userTitle.text = response.model1.title
                    completed.text = response.model1.completed.toString()
                } else {

                    response.model1.errorMessage?.let {

                        Toast.makeText(
                            this@MvvMCoroutinesActivity,
                            "Request model 1 failed, reason: code ${it.statusCode} : ${it.message}",
                            Toast.LENGTH_SHORT
                        ).show()

                    }?: kotlin.run {
                        Toast.makeText(
                            this@MvvMCoroutinesActivity,
                            "Request model 1 failed, reason: ${response.model1.errorMessage?.error}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                if (!response.model2.requestFailed()) {
                    Toast.makeText(this@MvvMCoroutinesActivity,
                        "request succeed model2!",
                        Toast.LENGTH_SHORT
                    ).show()

                    userId2.text = response.model2.userID.toString()
                    id2.text = response.model2.id.toString()
                    userTitle2.text = response.model2.title
                    completed2.text = response.model2.completed.toString()
                } else {

                    response.model2.errorMessage?.let {

                        Toast.makeText(
                            this@MvvMCoroutinesActivity,
                            "Request model 2 failed, reason: code ${it.statusCode} : ${it.message}",
                            Toast.LENGTH_SHORT
                        ).show()

                    }?: kotlin.run {
                        Toast.makeText(
                            this@MvvMCoroutinesActivity,
                            "Request model 2 failed, reason: ${response.model2.errorMessage?.error}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }

    private fun makeCallWithCoroutine()
    {
        // Make calls through coroutine scope in the main dispatcher
        CoroutineScope(Dispatchers.Main).launch {

            DemoViewModel().getAsyncDemo().observe(this@MvvMCoroutinesActivity, Observer { response ->

                binding?.apply {
                    if (!response.model1.requestFailed()) {
                        Toast.makeText(this@MvvMCoroutinesActivity,
                            "request succeed model 1!",
                            Toast.LENGTH_SHORT
                        ).show()

                        userId.text = response.model1.userID.toString()
                        id.text = response.model1.id.toString()
                        userTitle.text = response.model1.title
                        completed.text = response.model1.completed.toString()
                    } else {

                        response.model1.errorMessage?.let {

                            Toast.makeText(
                                this@MvvMCoroutinesActivity,
                                "Request model 1 failed, reason: code ${it.statusCode} : ${it.message}",
                                Toast.LENGTH_SHORT
                            ).show()

                        }?: kotlin.run {
                            Toast.makeText(
                                this@MvvMCoroutinesActivity,
                                "Request model 1 failed, reason: ${response.model1.errorMessage?.error}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    if (!response.model2.requestFailed()) {
                        Toast.makeText(this@MvvMCoroutinesActivity,
                            "request succeed model2!",
                            Toast.LENGTH_SHORT
                        ).show()

                        userId2.text = response.model2.userID.toString()
                        id2.text = response.model2.id.toString()
                        userTitle2.text = response.model2.title
                        completed2.text = response.model2.completed.toString()
                    } else {

                        response.model2.errorMessage?.let {

                            Toast.makeText(
                                this@MvvMCoroutinesActivity,
                                "Request model 2 failed, reason: code ${it.statusCode} : ${it.message}",
                                Toast.LENGTH_SHORT
                            ).show()

                        }?: kotlin.run {
                            Toast.makeText(
                                this@MvvMCoroutinesActivity,
                                "Request model 2 failed, reason: ${response.model2.errorMessage?.error}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            })
        }
    }
}