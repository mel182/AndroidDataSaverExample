package com.example.datasaverexampleapp.mvvm_coroutine_flow_livedata

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_mvv_m_coroutines.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MvvMCoroutinesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvv_m_coroutines)

        CoroutineScope(Dispatchers.Main).launch {

            DemoViewModel().getAsyncDemo().observe(this@MvvMCoroutinesActivity, Observer { response ->

                if (!response.model1.requestFailed()) {
                    Toast.makeText(this@MvvMCoroutinesActivity,
                        "request succeed model 1!",
                        Toast.LENGTH_SHORT
                    ).show()

                    user_id?.text = response.model1.userID.toString()
                    id?.text = response.model1.id.toString()
                    user_title?.text = response.model1.title
                    completed?.text = response.model1.completed.toString()

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

                    user_id2?.text = response.model2.userID.toString()
                    id2?.text = response.model2.id.toString()
                    user_title2?.text = response.model2.title
                    completed2?.text = response.model2.completed.toString()

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
            })
        }
    }
}