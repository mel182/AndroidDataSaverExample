package com.example.datasaverexampleapp.animation.activity_transition

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_transition2.*

class ActivityTransition2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition2)

        activity1_button?.setOnClickListener {
            onBackPressed()
        }
    }
}